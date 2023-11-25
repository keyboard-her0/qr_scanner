package com.keyboardhero.qr.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keyboardhero.qr.databinding.FragmentBaseBinding

/**
 * This abstract class should be used for all bottom sheet dialog fragment.
 *
 * @param VB : the [ViewBinding].
 * @property baseActivity the [BaseActivity].
 * @property baseBinding the [FragmentBaseBinding].
 * @property binding the [VB].
 * @property bindingInflater the logic binding inflate.
 */
abstract class BaseBottomSheetDialogFragment<VB : ViewBinding> :
    BottomSheetDialogFragment(),
    IBaseFragment {
    override val baseActivity: BaseActivity<*>?
        get() = activity as? BaseActivity<*>

    private lateinit var baseBinding: FragmentBaseBinding

    lateinit var binding: VB
        private set
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        baseBinding = FragmentBaseBinding.inflate(inflater, container, false)
        binding = bindingInflater.invoke(inflater, baseBinding.contentContainer, true)
        initViews()
        initActions()

        return baseBinding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Make parent background transparent for showing rounded corner border of header
        (view.parent as View).setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent,
            ),
        )

        initData(arguments)
        initObservers()

        (requireActivity() as OnBackPressedDispatcherOwner)
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) {
                onBackPressed()
            }
    }

    override fun showLoading() {
        baseBinding.loadingView.root.isVisible = true
    }

    override fun hideLoading() {
        baseBinding.loadingView.root.isVisible = false
    }

    override fun onBackPressed() {
        if (!findNavController().popBackStack()) {
            requireActivity().finish()
        }
    }
}
