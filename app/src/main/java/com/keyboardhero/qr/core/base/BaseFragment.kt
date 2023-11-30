package com.keyboardhero.qr.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.keyboardhero.qr.core.router.Router
import com.keyboardhero.qr.databinding.FragmentBaseBinding
import com.keyboardhero.qr.features.widget.AppBarWidget
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding> : Fragment(), IBaseFragment {
    override val baseActivity: BaseActivity<*>?
        get() = activity as? BaseActivity<*>

    private lateinit var baseBinding: FragmentBaseBinding

    protected val headerAppBar: AppBarWidget
        get() = baseBinding.appBar

    lateinit var binding: VB
        private set
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    @Inject
    lateinit var router: Router

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        baseBinding = FragmentBaseBinding.inflate(inflater, container, false)
        binding = bindingInflater.invoke(inflater, baseBinding.contentContainer, true)
        return baseBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            initData(arguments)
        }
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initHeaderAppBar()
        initActions()
        initObservers()

        (requireActivity() as OnBackPressedDispatcherOwner)
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) {
                onBackPressed()
            }
    }

    override fun showLoading() {
        baseActivity?.showLoading()
    }

    override fun hideLoading() {
        baseActivity?.hideLoading()
    }

    override fun onBackPressed() {
        if (!router.backToPreviousScreen()) {
            requireActivity().finish()
        }
    }

    /**
     * Adds a [Fragment] to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment        The fragment to be added.
     * @param addBackStack    True/false add back stack.
     */
    protected open fun replaceFragment(
        containerViewId: Int,
        fragment: Fragment?,
        addBackStack: Boolean,
    ) {
        val fragmentTransaction = this.childFragmentManager.beginTransaction()
        fragmentTransaction.replace(containerViewId, fragment!!)
        if (addBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

    /**
     * This method using to back to parent fragment from child's
     */
    protected open fun popBackStackManager() {
        val fragmentManager = parentFragmentManager
        if (fragmentManager.backStackEntryCount != 0) {
            fragmentManager.popBackStack()
        }
    }

    internal fun reCreateActivitySmooth() {
        baseActivity?.reCreateActivitySmooth()
    }
}
