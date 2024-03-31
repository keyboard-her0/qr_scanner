package com.keyboardhero.qr.features.create.result

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.core.utils.CommonUtils
import com.keyboardhero.qr.databinding.FragmentGenerateResultBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GenerateResultFragment : BaseFragment<FragmentGenerateResultBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGenerateResultBinding
        get() = FragmentGenerateResultBinding::inflate

    private val args: GenerateResultFragmentArgs by navArgs()
    private val viewModel: GenerateResultViewModel by viewModels()

    override fun initData(data: Bundle?) {
        viewModel.setup(type = args.type, barcodeData = args.barcodeData)
    }

    override fun initViews() {

    }

    override fun initHeaderAppBar() {
        headerAppBar.title = getString(R.string.title_generate_result_screen)
        headerAppBar.navigationIconId = R.drawable.ic_back_24
    }

    override fun initActions() {
        headerAppBar.navigationOnClickListener = {
            onBackPressed()
        }

        binding.btnShare.setOnClickListener {
            handleShareQr()
        }

        binding.btnSave.setOnClickListener {

        }
    }

    private fun handleShareQr() {
        val path = MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            viewModel.currentState.bitmap,
            "temp-file_qr",
            null
        )
        val uri = Uri.parse(path)
        val intent = CommonUtils.createIntentShareUriData(uri, "Chia sẻ với")
        startActivity(intent)
    }

    override fun initObservers() {
        viewModel.observe(
            owner = viewLifecycleOwner,
            selector = { state -> state.bitmap },
            observer = { bitmap ->
                Glide.with(requireContext()).load(bitmap).into(binding.imgQr)
            }
        )
    }
}