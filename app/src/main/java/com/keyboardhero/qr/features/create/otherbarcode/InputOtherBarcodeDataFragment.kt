package com.keyboardhero.qr.features.create.otherbarcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.keyboardhero.qr.R
import com.keyboardhero.qr.databinding.FragmentOtherBarcodeDataBinding
import com.keyboardhero.qr.features.create.CreateScreen
import com.keyboardhero.qr.features.create.input.BaseInputFragment
import com.keyboardhero.qr.features.create.result.GenerateResultFragmentArgs
import com.keyboardhero.qr.features.create.result.GenerateResultScreen
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import com.keyboardhero.qr.shared.domain.dto.barcodedata.AztecBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.DataMatrixBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.Pdf417Barcode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputOtherBarcodeDataFragment : BaseInputFragment<FragmentOtherBarcodeDataBinding>() {
    override val title: String
        get() = getString(R.string.app_name)
    override val bottomLayoutContainer: View
        get() = binding.layoutBottom

    private val args: InputOtherBarcodeDataFragmentArgs by navArgs()
    private lateinit var barcodeType: BarcodeType
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOtherBarcodeDataBinding
        get() = FragmentOtherBarcodeDataBinding::inflate

    override fun initDataInput(data: Bundle?) {
        barcodeType = args.barcodeType
    }

    override fun initViewsInput() {
        headerAppBar.title = getString(barcodeType.typeNameResId)

        buttonCreate?.isEnabled = binding.editText.text?.isNotBlank() == true
        binding.editText.doAfterTextChanged {
            buttonCreate?.isEnabled = binding.editText.text?.isNotBlank() == true
        }
    }

    override fun initObserversInput() {

    }

    override fun navigateToResultScreen() {
        val value = binding.editText.text.toString()
        val barcodeData = when (barcodeType) {
            BarcodeType.Aztec -> AztecBarcode(value)
            BarcodeType.DataMatrix -> DataMatrixBarcode(value)
            BarcodeType.Pdf417 -> Pdf417Barcode(value)
            else -> null
        }

        if (barcodeData != null) {
            router.navigate(
                GenerateResultScreen,
                GenerateResultFragmentArgs(
                    barcodeData = barcodeData,
                    type = barcodeType
                ).toBundle(),
                navOptions {
                    popUpTo(CreateScreen.getScreenId()) {
                        inclusive = false
                    }
                }
            )
        }

    }
}