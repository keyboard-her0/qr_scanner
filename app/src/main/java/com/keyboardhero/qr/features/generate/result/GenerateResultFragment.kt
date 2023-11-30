package com.keyboardhero.qr.features.generate.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.databinding.FragmentGenerateResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenerateResultFragment : BaseFragment<FragmentGenerateResultBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGenerateResultBinding
        get() = FragmentGenerateResultBinding::inflate

    private val viewModel: GenerateResultViewModel by viewModels()
    override fun initData(data: Bundle?) {

    }

    override fun initViews() {
    }

    override fun initHeaderAppBar() {
        headerAppBar.title = getString(R.string.title_generate_result_screen)
        headerAppBar.titleCentered = true
        headerAppBar.navigationIconId = R.drawable.ic_back_24
    }

    override fun initActions() {
        headerAppBar.navigationOnClickListener = {
            onBackPressed()
        }

        binding.btnSave.setOnClickListener {
            viewModel.generateQrFromString("10002")
        }
    }

    override fun initObservers() {
        viewModel.observe(
            owner = viewLifecycleOwner,
            selector = { state -> state.loading },
            observer = { loading ->
                if (loading) showLoading() else hideLoading()
            }
        )

        viewModel.observeEvent(
            lifecycleScope = lifecycleScope,
            viewLifecycleOwner = viewLifecycleOwner
        ) { event ->
            when (event) {
                GenerateResultViewEvents.GenerateQRFail -> {
                    showSingleOptionDialog(
                        title = "Lỗi",
                        message = "Lỗi rồi thử lại",
                        button = "Đóng",
                        listener = {
                            onBackPressed()
                        }
                    )
                }

                is GenerateResultViewEvents.GenerateQRSuccess -> {
                    binding.imgQr.setImageBitmap(event.bitmap)
                }
            }
        }
    }
}