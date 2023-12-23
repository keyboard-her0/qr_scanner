package com.keyboardhero.qr.features.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
import android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
import androidx.core.view.isVisible
import com.keyboardhero.qr.R
import com.keyboardhero.qr.databinding.LayoutDialogLoadingBinding

class ProcessDialog(
    context: Context,
    private val title: String = context.getString(R.string.dialog_process_title),
    private val message: String? = context.getString(R.string.dialog_process_message)
) : Dialog(context) {
    private val binding = LayoutDialogLoadingBinding.inflate(LayoutInflater.from(context))

    override fun show() {
        setCancelable(false)
        setContentView(binding.root)

        with(binding) {
            tvTitle.text = title
            tvMessenger.text = message
            tvMessenger.isVisible = !message.isNullOrEmpty()
        }
        setWindowDialog(this)
        super.show()
    }

    private fun setWindowDialog(dialog: Dialog) {
        val window: Window? = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val windowAttribute: WindowManager.LayoutParams? = window?.attributes
        windowAttribute?.gravity = Gravity.CENTER
        window?.attributes = windowAttribute
    }
}