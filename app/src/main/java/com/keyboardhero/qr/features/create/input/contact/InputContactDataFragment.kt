package com.keyboardhero.qr.features.create.input.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.keyboardhero.qr.R
import com.keyboardhero.qr.databinding.FragmentInputContactDataBinding
import com.keyboardhero.qr.features.create.CreateScreen
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
                    firstName = binding.editFirstName.text.toString(),
                    lastName = binding.editLastName.text.toString(),
                    phoneNumber = binding.inputPhone.text.trim(),
                    email = binding.inputEmail.text.trim(),
                    company = binding.editCompany.text.toString(),
                    jobTitle = binding.editJob.text.toString(),
                    country = binding.editCountry.text.toString(),
                    city = binding.editCity.text.toString(),
                    zip = binding.editZipCode.text.toString(),
                    state = binding.editState.text.toString(),
                    street = binding.editStreet.text.toString(),
                    website = binding.inputWebsite.text.trim()
                ),
                type = BarcodeType.Contact,
            ).toBundle(),
            navOptions {
                popUpTo(CreateScreen.getScreenId()) {
                    inclusive = false
                }
            }
        )
    }
}