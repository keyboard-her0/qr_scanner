package com.keyboardhero.qr.features.create.input.text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.keyboardhero.qr.R
import com.keyboardhero.qr.databinding.FragmentInputTextDataBinding
import com.keyboardhero.qr.features.create.input.BaseInputFragment
import com.keyboardhero.qr.features.create.result.GenerateResultFragmentArgs
import com.keyboardhero.qr.features.create.result.GenerateResultScreen
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import com.keyboardhero.qr.shared.domain.dto.barcodedata.TextBarcode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputTextDataFragment : BaseInputFragment<FragmentInputTextDataBinding>() {
    override val title: String
        get() = getString(R.string.text)
    override val bottomLayoutContainer: View
        get() = binding.layoutBottom
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInputTextDataBinding
        get() = FragmentInputTextDataBinding::inflate

    override fun initDataInput(data: Bundle?) {

    }

    override fun initViewsInput() {
        binding.editText.doAfterTextChanged {
            bottomBinding?.btnCreate?.isEnabled = it?.isNotBlank() == true
        }
    }

    override fun initObserversInput() {

    }

    override fun navigateToResultScreen() {
        router.navigate(
            GenerateResultScreen,
            GenerateResultFragmentArgs(
                barcodeData = TextBarcode(
                    value = binding.editText.text.toString()
                ),
                type = BarcodeType.Text
            ).toBundle()
        )
    }
}