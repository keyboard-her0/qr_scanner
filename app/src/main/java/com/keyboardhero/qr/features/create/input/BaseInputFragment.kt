package com.keyboardhero.qr.features.create.input

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.CallSuper
import androidx.core.view.setPadding
import androidx.viewbinding.ViewBinding
import com.google.android.material.button.MaterialButton
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.core.utils.CommonUtils

abstract class BaseInputFragment<VB : ViewBinding> : BaseFragment<VB>(), IBaseInputDataFragment {

    abstract val title: String

    abstract val bottomLayoutContainer: View

    protected var buttonCreate: MaterialButton? = null

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
        val context = requireContext()
        buttonCreate = MaterialButton(context).apply {
            text = getString(R.string.create)
            setOnClickListener {
                navigateToResultScreen()
            }
            textSize = 18F
            setTextColor(Color.WHITE)
            setPadding(context.resources.getDimensionPixelSize(R.dimen.size_16dp))
        }
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        (bottomLayoutContainer as? FrameLayout)?.addView(buttonCreate, layoutParams)
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
        initViewsInput()
        initActionsInput()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserversInput()
    }
}