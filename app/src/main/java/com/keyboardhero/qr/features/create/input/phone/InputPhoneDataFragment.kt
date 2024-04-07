package com.keyboardhero.qr.features.create.input.phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.keyboardhero.qr.R
import com.keyboardhero.qr.databinding.FragmentInputPhoneDataBinding
import com.keyboardhero.qr.features.create.input.BaseInputFragment
import com.keyboardhero.qr.features.create.result.GenerateResultFragmentArgs
import com.keyboardhero.qr.features.create.result.GenerateResultScreen
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import com.keyboardhero.qr.shared.domain.dto.barcodedata.PhoneBarcode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputPhoneDataFragment : BaseInputFragment<FragmentInputPhoneDataBinding>() {
    override val title: String
        get() = getString(R.string.phone)
    override val bottomLayoutContainer: View
        get() = binding.layoutBottom
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInputPhoneDataBinding
        get() = FragmentInputPhoneDataBinding::inflate

    override fun initDataInput(data: Bundle?) {

    }

    override fun initViewsInput() {

    }

    override fun initObserversInput() {

    }

    override fun navigateToResultScreen() {
        router.navigate(
            GenerateResultScreen,
            GenerateResultFragmentArgs(
                barcodeData = PhoneBarcode(
                    phoneNumber = binding.inputPhoneNumber.text.trim()
                ),
                type = BarcodeType.Phone
            ).toBundle()
        )
    }
}