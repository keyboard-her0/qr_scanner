package com.keyboardhero.qr.features.create.result

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.core.utils.CommonUtils
import com.keyboardhero.qr.core.utils.views.onSafeClick
import com.keyboardhero.qr.databinding.FragmentGenerateResultBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class GenerateResultFragment : BaseFragment<FragmentGenerateResultBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGenerateResultBinding
        get() = FragmentGenerateResultBinding::inflate

    private val args: GenerateResultFragmentArgs by navArgs()
    private val viewModel: GenerateResultViewModel by viewModels()
    private var isCreateNew: Boolean = true

    override fun initData(data: Bundle?) {
        isCreateNew = args.isCreateNew
        viewModel.setup(
            type = args.type,
            barcodeData = args.barcodeData,
            isCreateNew = isCreateNew
        )
    }

    override fun initViews() {

    }

    override fun initHeaderAppBar() {
        headerAppBar.title = getString(
            if (isCreateNew) R.string.title_generate_result_screen else R.string.detail
        )
        headerAppBar.titleCentered = true
        headerAppBar.navigationIconId = R.drawable.ic_back_24
    }

    override fun initActions() {
        headerAppBar.navigationOnClickListener = {
            onBackPressed()
        }

        binding.btnShare.onSafeClick {
            handleShareQr()
        }

        binding.btnSave.onSafeClick {
            handleSaveImage()
        }
    }

    private fun handleSaveImage() {
        viewModel.currentState.bitmap?.let { bitmap ->
            lifecycleScope.launch {
                val result = withContext(Dispatchers.IO) {
                    CommonUtils.saveImage(bitmap, requireContext())
                }
                Toast.makeText(
                    requireContext(),
                    if (result) "Lưu ảnh thành công" else "Lưu ảnh thất bại",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleShareQr() {
        val path = MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            viewModel.currentState.bitmap,
            "temp-file_qr",
            null
        )
        if (path != null) {
            val uri = Uri.parse(path)
            val intent = CommonUtils.createIntentShareUriData(uri, "Chia sẻ với")
            startActivity(intent)
        }
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