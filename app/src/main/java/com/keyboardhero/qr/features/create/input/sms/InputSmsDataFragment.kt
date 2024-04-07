package com.keyboardhero.qr.features.create.input.sms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navOptions
import com.keyboardhero.qr.R
import com.keyboardhero.qr.databinding.FragmentInputSmsDataBinding
import com.keyboardhero.qr.features.create.CreateScreen
import com.keyboardhero.qr.features.create.input.BaseInputFragment
import com.keyboardhero.qr.features.create.result.GenerateResultFragmentArgs
import com.keyboardhero.qr.features.create.result.GenerateResultScreen
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import com.keyboardhero.qr.shared.domain.dto.barcodedata.SmsBarcode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputSmsDataFragment : BaseInputFragment<FragmentInputSmsDataBinding>() {
    override val title: String
        get() = getString(R.string.sms)
    override val bottomLayoutContainer: View
        get() = binding.layoutBottom
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInputSmsDataBinding
        get() = FragmentInputSmsDataBinding::inflate

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
                barcodeData = SmsBarcode(
                    phoneNumber = binding.inputView.text.trim(),
                    message = binding.editTextMessage.text.toString()
                ),
                type = BarcodeType.Sms
            ).toBundle(),
            navOptions {
                popUpTo(CreateScreen.getScreenId()) {
                    inclusive = false
                }
            }
        )
    }
}