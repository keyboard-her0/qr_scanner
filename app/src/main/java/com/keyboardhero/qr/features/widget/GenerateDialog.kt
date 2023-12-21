package com.keyboardhero.qr.features.widget

import android.app.Dialog
import android.view.LayoutInflater
import com.keyboardhero.qr.core.base.BaseDialogFragment
import com.keyboardhero.qr.databinding.LayoutDialogGenerateBinding

class GenerateDialog : BaseDialogFragment() {

    private lateinit var binding: LayoutDialogGenerateBinding
    override fun setContentDialog(dialog: Dialog) {
        binding = LayoutDialogGenerateBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(binding.root)
    }

    override fun initListeners(dialog: Dialog) {

    }
}