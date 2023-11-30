package com.keyboardhero.qr.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.keyboardhero.qr.databinding.FragmentBaseBinding
import com.keyboardhero.qr.features.widget.AppBarWidget
import com.keyboardhero.qr.features.widget.ProcessDialog

abstract class BaseFragment<VB : ViewBinding> : Fragment(), IBaseFragment {
    override val baseActivity: BaseActivity<*>?
        get() = activity as? BaseActivity<*>

    private lateinit var baseBinding: FragmentBaseBinding

    protected val headerAppBar :AppBarWidget
        get() = baseBinding.appBar

    lateinit var binding: VB
        private set
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    protected var mNavController: NavController? = null
    private lateinit var mRootView: View

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        baseBinding = FragmentBaseBinding.inflate(inflater, container, false)
        binding = bindingInflater.invoke(inflater, baseBinding.contentContainer, true)

        mRootView = baseBinding.root
        // Remove view on the child's parent first.
        val parent = mRootView.parent
        if (parent != null) {
            parent as ViewGroup
            parent.removeView(mRootView)
        }
        return mRootView
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mNavController = (activity as? BaseActivity<*>)?.navController
        initViews()
        initHeaderAppBar()
        initActions()
        initData(arguments)
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
        if (!findNavController().popBackStack()) {
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

    /**
     * This method using to back to previous screen with used navigation controller
     */
    internal fun popBackStackNavigate() {
        mNavController?.popBackStack()
    }
}
