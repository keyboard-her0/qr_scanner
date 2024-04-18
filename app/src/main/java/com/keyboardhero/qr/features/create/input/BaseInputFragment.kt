package com.keyboardhero.qr.features.create.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.core.utils.CommonUtils
import com.keyboardhero.qr.databinding.LayoutBottomInputBinding

abstract class BaseInputFragment<VB : ViewBinding> : BaseFragment<VB>(), IBaseInputDataFragment {

    protected var bottomBinding: LayoutBottomInputBinding? = null


    abstract val title: String

    abstract val bottomLayoutContainer: View

    final override fun initData(data: Bundle?) {

    }

    final override fun initHeaderAppBar() {
        headerAppBar.title = title
        headerAppBar.titleCentered = true
        headerAppBar.navigationIconId = R.drawable.ic_back_24
    }

    final override fun initActions() {
        headerAppBar.navigationOnClickListener = {
            onBackPressed()
        }
    }

    override fun initActionsInput() {
        bottomBinding?.btnCreate?.setOnClickListener {
            navigateToResultScreen()
        }
    }

    final override fun initObservers() {

    }

    final override fun initViews() {
        val padding = requireContext().resources.getDimensionPixelOffset(R.dimen.size_16dp)
        bottomLayoutContainer.setPadding(
            padding,
            padding,
            padding,
            padding + CommonUtils.getNavigationBarHeight(requireContext())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataInput(savedInstanceState)
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        (bottomLayoutContainer as? FrameLayout)?.apply {
            bottomBinding = LayoutBottomInputBinding.inflate(inflater, this, true)
        }
        initViewsInput()
        initActionsInput()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserversInput()
    }
}