package com.keyboardhero.qr.features.scan.resutl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.databinding.FragmentResultScanBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultScanFragment : BaseFragment<FragmentResultScanBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentResultScanBinding
        get() = FragmentResultScanBinding::inflate

    private val args: ResultScanFragmentArgs by navArgs()

    private val viewModel: ResultScanViewModel by viewModels()

    override fun initData(data: Bundle?) {
        viewModel.iniData(args.scanData)
    }

    override fun initViews() {
        //Do nothing
    }

    override fun initHeaderAppBar() {
        headerAppBar.title = getString(R.string.result)
        headerAppBar.titleCentered = true
        headerAppBar.navigationIconId = R.drawable.ic_back_24
    }

    override fun initActions() {
        headerAppBar.navigationOnClickListener = {
            onBackPressed()
        }
    }

    override fun initObservers() {
        viewModel.observe(
            owner = viewLifecycleOwner,
            selector = { state -> state.barcodeData },
            observer = {
                binding.tvData.text = it
            }
        )
    }
}