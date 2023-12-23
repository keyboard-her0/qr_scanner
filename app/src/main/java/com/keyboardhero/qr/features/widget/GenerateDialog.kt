package com.keyboardhero.qr.features.widget

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseDialogFragment
import com.keyboardhero.qr.core.utils.views.hideKeyboard
import com.keyboardhero.qr.databinding.LayoutDialogGenerateBinding
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import com.keyboardhero.qr.shared.domain.dto.barcodedata.BarcodeData
import com.keyboardhero.qr.shared.domain.dto.barcodedata.TextBarcode

class GenerateDialog : BaseDialogFragment<LayoutDialogGenerateBinding>() {
    override fun getViewBinding(): LayoutDialogGenerateBinding {
        return LayoutDialogGenerateBinding.inflate(LayoutInflater.from(context))
    }

    private var type: BarcodeType = BarcodeType.TEXT

    private var barcodeData: BarcodeData? = null

    override fun initData(arguments: Bundle?) {
        super.initData(arguments)
        type = arguments?.get(TYPE) as? BarcodeType ?: type
    }

    private var listener: DialogListener? = null

    override fun initAction() {
        super.initAction()
        with(binding) {
            btnClose.setOnClickListener {
                binding.edtData.hideKeyboard()
                dismiss()
                listener?.onNegativeClick()
            }

            btnOk.setOnClickListener {
                binding.edtData.hideKeyboard()
                dismiss()
                barcodeData = TextBarcode(binding.edtData.text.toString())
                barcodeData?.let { data -> listener?.onPositiveClick(data, type) }
            }
        }
    }

    override fun initView() {
        super.initView()
        with(binding) {

            when (type) {
                BarcodeType.TEXT -> {
                    layoutEdtData.hint = getString(R.string.enter_text)
                }

                BarcodeType.URL -> {
                    layoutEdtData.hint = getString(R.string.enter_url)
                }

                BarcodeType.EMAIl -> {
                    layoutEdtData.hint = getString(R.string.enter_email)
                }

                BarcodeType.SMS -> {
                    layoutEdtData.hint = getString(R.string.enter_message)
                }

                BarcodeType.WIFI -> {
                    layoutEdtData.hint = getString(R.string.enter_ssid)
                }

                BarcodeType.VCARD -> {

                }
            }
        }
    }

    companion object {
        fun newInstance(
            type: BarcodeType,
            listener: DialogListener
        ): GenerateDialog {
            return GenerateDialog().apply {
                this.listener = listener
                arguments = bundleOf(
                    TYPE to type
                )
            }
        }

        private const val TYPE = "TYPE_KEY"
    }

    interface DialogListener {
        fun onPositiveClick(barcodeData: BarcodeData, barcodeType: BarcodeType)

        fun onNegativeClick()
    }
}