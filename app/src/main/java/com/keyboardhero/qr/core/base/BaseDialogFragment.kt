package com.keyboardhero.qr.core.base

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager.LayoutParams
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

abstract class BaseDialogFragment : DialogFragment() {
    companion object {
        private const val dimValue = 0.5f

        @Volatile
        private var isShowing = false
    }

    var onBackPressCallback: () -> Unit = {}

    override fun onStart() {
        super.onStart()
        setLayoutSize()
    }

    private fun setLayoutSize() {
        val width = (resources.displayMetrics.widthPixels * 0.92).toInt()
        val height = LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext()) {
            override fun onBackPressed() {
                onBackPressCallback()
            }
        }.apply {
            window?.run {
                requestFeature(Window.FEATURE_NO_TITLE)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setDimAmount(dimValue)
            }
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setContentDialog(this)
            initListeners(this)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isShowing && !isAdded) {
            isShowing = true
            super.show(manager, tag)
        }
    }

    override fun dismiss() {
        if (isShowing && isAdded) {
            super.dismiss()
        }
    }

    /**
     * Handle callback when dismiss dialog
     *
     * @param dialog [DialogInterface]
     */
    override fun onDismiss(dialog: DialogInterface) {
        if (isShowing) {
            isShowing = false
            super.onDismiss(dialog)
        }
    }

    /**
     * This method using to set content view of Dialog
     */
    abstract fun setContentDialog(dialog: Dialog)

    abstract fun initListeners(dialog: Dialog)
}
