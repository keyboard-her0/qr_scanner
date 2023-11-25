package com.keyboardhero.qr.core.utils.views

import android.content.Context
import android.graphics.Rect
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Context.hideKeyboard(view: View) {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.showKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.isCloseKeyBoard(): Boolean {
    val rect = Rect()
    getWindowVisibleDisplayFrame(rect)
    val screenHeight = rootView.height
    val keypadHeight = screenHeight - rect.bottom
    return (keypadHeight <= screenHeight * 0.15)
}

/**
 * Clear focus on key done.
 *
 * @param keyCode Int the key code.
 * @param keyEvent the [KeyEvent].
 * @return Boolean for on focus change listener.
 */
fun View.clearFocusOnKeyDone(keyCode: Int, keyEvent: KeyEvent): Boolean {
    if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
        clearFocus()
        hideKeyboard()
        return true
    }
    return false
}
