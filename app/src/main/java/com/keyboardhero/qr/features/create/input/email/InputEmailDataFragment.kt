package com.keyboardhero.qr.features.create.input.email

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.navOptions
import com.keyboardhero.qr.R
import com.keyboardhero.qr.databinding.FragmentInputEmailDataBinding
import com.keyboardhero.qr.features.create.CreateScreen
import com.keyboardhero.qr.features.create.input.BaseInputFragment
import com.keyboardhero.qr.features.create.result.GenerateResultFragmentArgs
import com.keyboardhero.qr.features.create.result.GenerateResultScreen
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import com.keyboardhero.qr.shared.domain.dto.barcodedata.EmailBarcode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputEmailDataFragment : BaseInputFragment<FragmentInputEmailDataBinding>() {
    override val title: String
        get() = getString(R.string.email)
    override val bottomLayoutContainer: View
        get() = binding.layoutBottom
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInputEmailDataBinding
        get() = FragmentInputEmailDataBinding::inflate

    override fun initDataInput(data: Bundle?) {

    }

    override fun initViewsInput() {
        checkValidInput()

        binding.inputEmail.editText.doAfterTextChanged {
            checkValidInput()
        }

        binding.editTextMessage.doAfterTextChanged {
            checkValidInput()
        }

        binding.editSubject.doAfterTextChanged {
            checkValidInput()
        }
    }

    override fun initObserversInput() {

    }

    private fun checkValidInput() {
        buttonCreate?.isEnabled =
            binding.editTextMessage.text?.isNotBlank() == true && binding.inputEmail.text.isNotBlank() && binding.editSubject.text?.isNotBlank() == true
    }

    override fun navigateToResultScreen() {
        router.navigate(
            GenerateResultScreen,
            GenerateResultFragmentArgs(
                barcodeData = EmailBarcode(
                    email = binding.inputEmail.text.trim(),
                    subject = binding.editSubject.text.toString().trim(),
                    message = binding.editTextMessage.text.toString().trim()
                ),
                type = BarcodeType.Email
            ).toBundle(),
            navOptions {
                popUpTo(CreateScreen.getScreenId()) {
                    inclusive = false
                }
            }
        )
    }
}