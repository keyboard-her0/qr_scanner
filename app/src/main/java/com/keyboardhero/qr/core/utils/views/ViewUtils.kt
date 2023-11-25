package com.keyboardhero.qr.core.utils.views

import android.content.res.Configuration
import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment

/**
 * Set view margin
 */
fun View.setMargin(
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null,
    start: Int? = null,
    end: Int? = null
) {
    (layoutParams as? ViewGroup.MarginLayoutParams?)?.apply {
        setMargins(
            left ?: marginLeft,
            top ?: marginTop,
            right ?: marginRight,
            bottom ?: marginBottom
        )
        marginStart = start ?: marginStart
        marginEnd = end ?: marginEnd
        requestLayout()
    }
}

fun ViewGroup.saveChildViewStates(): SparseArray<Parcelable> {
    val childViewStates = SparseArray<Parcelable>()
    children.forEach { child -> child.saveHierarchyState(childViewStates) }
    return childViewStates
}

fun ViewGroup.restoreChildViewStates(childViewStates: SparseArray<Parcelable>) {
    children.forEach { child -> child.restoreHierarchyState(childViewStates) }
}

fun View.onSafeClick(onSafeClick: (View) -> Unit) {
    val safetyClickListener = SafetyClickListener()
    safetyClickListener.setViewClickSafetyListener(this) {
        onSafeClick(it)
    }
    setOnClickListener(safetyClickListener)
}

val Fragment.isNightMode: Boolean
    get() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
        Configuration.UI_MODE_NIGHT_YES
