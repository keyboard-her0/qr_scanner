package com.keyboardhero.qr.features.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.utils.CommonUtils.getStatusBarHeight
import com.keyboardhero.qr.databinding.LayoutToolBarBinding

class AppBarWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppBarLayout(context, attributeSet, defStyleAttr) {

    private val binding = LayoutToolBarBinding.inflate(LayoutInflater.from(context), this, true)

    var title: String = ""
        set(value) {
            field = value
            binding.toolBar.title = value
        }

    var titleCentered: Boolean = false
        set(value) {
            field = value
            binding.toolBar.isTitleCentered = value
        }

    init {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.AppBarWidget,
            0,
            0,
        ).apply {
            try {
                title = getString(R.styleable.AppBarWidget_title) ?: ""
                titleCentered = getBoolean(R.styleable.AppBarWidget_titleCentered, false)
                binding.toolBar.title = title
                binding.toolBar.isTitleCentered = titleCentered
            } finally {
                recycle()
            }
        }

        binding.root.setPadding(0, getStatusBarHeight(context), 0, 0)
    }

    fun getToolBar(): MaterialToolbar = binding.toolBar


}