package com.keyboardhero.qr.features.create.input.url

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.keyboardhero.qr.R
import com.keyboardhero.qr.databinding.FragmentInputUrlDataBinding
import com.keyboardhero.qr.features.create.input.BaseInputFragment
import com.keyboardhero.qr.features.create.result.GenerateResultFragmentArgs
import com.keyboardhero.qr.features.create.result.GenerateResultScreen
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import com.keyboardhero.qr.shared.domain.dto.barcodedata.UrlBarcode
import dagger.hilt.android.AndroidEntryPoint

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
            ).toBundle()
        )
    }
}