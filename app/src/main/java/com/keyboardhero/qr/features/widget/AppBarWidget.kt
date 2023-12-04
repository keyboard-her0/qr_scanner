package com.keyboardhero.qr.features.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.utils.CommonUtils.getStatusBarHeight
import com.keyboardhero.qr.core.utils.views.dp
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

    var navigationIconId: Int = 0
        set(value) {
            field = value
            binding.toolBar.navigationIcon = getDrawableFromId(context, navigationIconId)
        }

    var navigationOnClickListener: (() -> Unit)? = null

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
                navigationIconId =
                    getResourceId(R.styleable.AppBarWidget_navigationIcon, navigationIconId)

                binding.toolBar.title = title
                binding.toolBar.isTitleCentered = titleCentered
                binding.toolBar.navigationIcon = getDrawableFromId(context, navigationIconId)
            } finally {
                recycle()
            }
        }

        binding.root.setPadding(0, getStatusBarHeight(context), 0, 0)
        binding.toolBar.setNavigationOnClickListener {
            navigationOnClickListener?.invoke()
        }
    }

    fun getToolBar(): MaterialToolbar = binding.toolBar

    private fun getDrawableFromId(context: Context, resId: Int): Drawable? {
        return try {
            ContextCompat.getDrawable(context, resId)
        } catch (e: Exception) {
            null
        }
    }
}