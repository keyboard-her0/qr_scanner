package com.keyboardhero.qr.features.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.zxing.BarcodeFormat
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.core.router.Screen
import com.keyboardhero.qr.databinding.FragmentCreateBinding
import com.keyboardhero.qr.features.create.input.contact.InputContactScreen
import com.keyboardhero.qr.features.create.input.email.InputEmailScreen
import com.keyboardhero.qr.features.create.input.location.InputLocationScreen
import com.keyboardhero.qr.features.create.input.phone.InputPhoneScreen
import com.keyboardhero.qr.features.create.input.sms.InputSmsScreen
import com.keyboardhero.qr.features.create.input.text.InputTextScreen
import com.keyboardhero.qr.features.create.input.url.InputUrlScreen
import com.keyboardhero.qr.features.create.input.wifi.InputWifiScreen
import com.keyboardhero.qr.features.create.otherbarcode.InputOtherBarCodeScreen
import com.keyboardhero.qr.features.create.otherbarcode.InputOtherBarcodeDataFragmentArgs
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateFragment : BaseFragment<FragmentCreateBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateBinding
        get() = FragmentCreateBinding::inflate

    private val generateItemQRAdapter: BarCodeTypeItemAdapter by lazy { BarCodeTypeItemAdapter() }
    private val generateItemOtherAdapter: BarCodeTypeItemAdapter by lazy { BarCodeTypeItemAdapter() }
    private val viewModel: CreateViewModel by viewModels()

    override fun initData(data: Bundle?) {
    }

    override fun initViews() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(binding) {
            rvQRItem.adapter = generateItemQRAdapter
            rvQRItem.layoutManager = GridLayoutManager(requireContext(), 3)

            rvQROther.adapter = generateItemOtherAdapter
            rvQROther.layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    override fun initHeaderAppBar() {
        headerAppBar.title = getString(R.string.create)
        headerAppBar.navigationIconId = R.drawable.ic_back_24
        headerAppBar.titleCentered = true
    }

    override fun initActions() {
        headerAppBar.navigationOnClickListener = { onBackPressed() }
        generateItemQRAdapter.onItemClick = { barcodeType ->
            router.navigate(findScreen(barcodeType))
        }

        generateItemOtherAdapter.onItemClick = { barcodeType ->
            router.navigate(
                findScreen(barcodeType),
                InputOtherBarcodeDataFragmentArgs(
                    barcodeType = barcodeType
                ).toBundle()
            )
        }
    }

    private fun findScreen(barcodeType: BarcodeType): Screen {
        return when (barcodeType) {
            BarcodeType.Text -> InputTextScreen
            BarcodeType.Phone -> InputPhoneScreen
            BarcodeType.Sms -> InputSmsScreen
            BarcodeType.Contact -> InputContactScreen
            BarcodeType.Wifi -> InputWifiScreen
            BarcodeType.Email -> InputEmailScreen
            BarcodeType.Url -> InputUrlScreen
            BarcodeType.Location -> InputLocationScreen
            else -> InputOtherBarCodeScreen
        }
    }


    override fun initObservers() {
        viewModel.observe(
            owner = viewLifecycleOwner,
            selector = { state -> state.generateItem },
            observer = { generateItems ->
                val qrCodes = generateItems.filter { it.barcodeFormat == BarcodeFormat.QR_CODE }
                val otherCodes = generateItems.filter { it.barcodeFormat != BarcodeFormat.QR_CODE }
                generateItemQRAdapter.submitList(qrCodes)
                generateItemOtherAdapter.submitList(otherCodes)
            }
        )
    }
}