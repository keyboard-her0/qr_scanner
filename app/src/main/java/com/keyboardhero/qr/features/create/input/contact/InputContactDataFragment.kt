package com.keyboardhero.qr.features.create.input.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.keyboardhero.qr.R
import com.keyboardhero.qr.databinding.FragmentInputContactDataBinding
import com.keyboardhero.qr.features.create.input.BaseInputFragment
import com.keyboardhero.qr.features.create.result.GenerateResultFragmentArgs
import com.keyboardhero.qr.features.create.result.GenerateResultScreen
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import com.keyboardhero.qr.shared.domain.dto.barcodedata.ContactBarcode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputContactDataFragment : BaseInputFragment<FragmentInputContactDataBinding>() {
    override val title: String
        get() = getString(R.string.contact)
    override val bottomLayoutContainer: View
        get() = binding.layoutBottom
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInputContactDataBinding
        get() = FragmentInputContactDataBinding::inflate

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
                barcodeData = ContactBarcode(
                    name = binding.inputName.text,
                    phoneNumber = binding.inputPhone.text

                ),
                type = BarcodeType.SMS
            ).toBundle()
        )
    }
}