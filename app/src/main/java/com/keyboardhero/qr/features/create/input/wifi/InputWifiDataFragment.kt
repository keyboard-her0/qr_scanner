package com.keyboardhero.qr.features.create.input.wifi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.navOptions
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.utils.views.hideKeyboard
import com.keyboardhero.qr.databinding.FragmentInputWifiDataBinding
import com.keyboardhero.qr.features.create.CreateScreen
import com.keyboardhero.qr.features.create.input.BaseInputFragment
import com.keyboardhero.qr.features.create.result.GenerateResultFragmentArgs
import com.keyboardhero.qr.features.create.result.GenerateResultScreen
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import com.keyboardhero.qr.shared.domain.dto.barcodedata.WifiBarcode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputWifiDataFragment : BaseInputFragment<FragmentInputWifiDataBinding>() {
    override val title: String
        get() = getString(R.string.wifi)
    override val bottomLayoutContainer: View
        get() = binding.layoutBottom
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInputWifiDataBinding
        get() = FragmentInputWifiDataBinding::inflate

    override fun initDataInput(data: Bundle?) {

    }

    private val listTypeSecurity = WifiBarcode.TypeSecurity.values().map { it.typeName }

    private var typeSecuritySelected = 0

    override fun initViewsInput() {
        checkValidInput()

        val adapterSecurity: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_selectable_list_item,
            listTypeSecurity
        )

        with(binding) {
            autoCompleteTextView.apply {
                setAdapter(adapterSecurity)
                setText(listTypeSecurity.getOrNull(typeSecuritySelected), false)
                setOnClickListener {
                    it.hideKeyboard()
                    autoCompleteTextView.showDropDown()
                }

                onItemClickListener =
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                        typeSecuritySelected = position
                    }
            }
        }

        binding.inputName.editText.doAfterTextChanged { checkValidInput() }
        binding.inputPassword.editText.doAfterTextChanged { checkValidInput() }
    }

    private fun checkValidInput() {
        buttonCreate?.isEnabled =
            binding.inputName.text.isNotBlank() && binding.inputPassword.text.isNotBlank()
    }

    override fun initObserversInput() {

    }

    override fun navigateToResultScreen() {
        router.navigate(
            GenerateResultScreen,
            GenerateResultFragmentArgs(
                barcodeData = WifiBarcode(
                    ssid = binding.inputName.text.trim(),
                    password = binding.inputPassword.text,
                    isHide = binding.cbHide.isChecked,
                    type = WifiBarcode.TypeSecurity.values()[typeSecuritySelected]
                ),
                type = BarcodeType.Wifi
            ).toBundle(),
            navOptions {
                popUpTo(CreateScreen.getScreenId()) {
                    inclusive = false
                }
            }
        )
    }
}