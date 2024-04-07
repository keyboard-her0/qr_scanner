package com.keyboardhero.qr.core.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

fun getColorFromAttr(
    context: Context,
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    context.theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

fun getResIdFromAttr(
    context: Context,
    @AttrRes attr: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    context.theme.resolveAttribute(attr, typedValue, resolveRefs)
    return typedValue.resourceId
}
