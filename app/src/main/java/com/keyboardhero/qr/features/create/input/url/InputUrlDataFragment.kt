package com.keyboardhero.qr.features.create.input.url

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.navOptions
import com.keyboardhero.qr.R
import com.keyboardhero.qr.databinding.FragmentInputUrlDataBinding
import com.keyboardhero.qr.features.create.CreateScreen
import com.keyboardhero.qr.features.create.input.BaseInputFragment
import com.keyboardhero.qr.features.create.result.GenerateResultFragmentArgs
import com.keyboardhero.qr.features.create.result.GenerateResultScreen
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import com.keyboardhero.qr.shared.domain.dto.barcodedata.UrlBarcode
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class InputUrlDataFragment : BaseInputFragment<FragmentInputUrlDataBinding>() {
    override val title: String
        get() = getString(R.string.website)
    override val bottomLayoutContainer: View
        get() = binding.layoutBottom
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInputUrlDataBinding
        get() = FragmentInputUrlDataBinding::inflate

    override fun initDataInput(data: Bundle?) {

    }

    override fun initViewsInput() {
        buttonCreate?.isEnabled = isValidURL(binding.inputUrl.text)
        binding.inputUrl.editText.doAfterTextChanged {
            buttonCreate?.isEnabled = isValidURL(binding.inputUrl.text)
        }
    }

    private fun isValidURL(url: String): Boolean {
        val urlPattern = ("^((https?|ftp)://)?"
                + "(([a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\(\\)\\,\\;\\?\\&\\=]|%[0-9a-fA-F]{2})+@)?"
                + "((([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9]\\.[a-zA-Z]{2,6})"
                + "(:[0-9]{1,5})?"
                + "((/([a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\(\\)\\,\\;\\?\\&\\=]|%[0-9a-fA-F]{2})*)+|/)?"
                + "(\\?([a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\(\\)\\,\\;\\?\\&\\=]|%[0-9a-fA-F]{2})*)?"
                + "(#([a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\(\\)\\,\\;\\?\\&\\=]|%[0-9a-fA-F]{2})*)?$")

        val pattern = Pattern.compile(urlPattern)
        val matcher = pattern.matcher(url)
        return matcher.matches()
    }

    override fun initObserversInput() {

    }

    override fun navigateToResultScreen() {
        router.navigate(
            GenerateResultScreen,
            GenerateResultFragmentArgs(
                barcodeData = UrlBarcode(
                    url = binding.inputUrl.text.trim()
                ),
                type = BarcodeType.Url
            ).toBundle(),
            navOptions {
                popUpTo(CreateScreen.getScreenId()) {
                    inclusive = false
                }
            }
        )
    }
}