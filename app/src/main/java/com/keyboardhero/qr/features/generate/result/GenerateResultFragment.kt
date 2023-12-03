package com.keyboardhero.qr.features.generate.result

import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Size
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.core.utils.CommonUtils
import com.keyboardhero.qr.core.utils.qr.QRUtils
import com.keyboardhero.qr.databinding.FragmentGenerateResultBinding
import com.keyboardhero.qr.shared.domain.dto.input.QrObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


@AndroidEntryPoint
class GenerateResultFragment : BaseFragment<FragmentGenerateResultBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGenerateResultBinding
        get() = FragmentGenerateResultBinding::inflate

    private val args: GenerateResultFragmentArgs by navArgs()
    private val viewModel: GenerateResultViewModel by viewModels()
    private var bitmapQr: Bitmap? = null

    override fun initData(data: Bundle?) {
        val qrInput = args.qrInput
        viewModel.setData(qrInput = qrInput)
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
        val size = resources.getDimensionPixelSize(R.dimen.size_240dp)
        val bitmapBackground = binding.imgQr.background.toBitmap()

        val bitmapResult = Bitmap.createBitmap(size, size, bitmapBackground.config)
        val canvas = Canvas(bitmapResult)
        canvas.drawBitmap(bitmapBackground, 0f, 0f, null)
        canvas.drawBitmap(bitmapQr!!, 0f, 0f, null)

        val path = MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            bitmapResult,
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
            selector = { state -> state.qrInput },
            observer = { qrInput ->
                if (qrInput != null) {
                    showQrCode(qrInput)
                }
            }
        )
    }

    private fun showQrCode(qrInput: QrObject) {
        lifecycleScope.launch {
            showLoading()
            val size = resources.getDimensionPixelSize(R.dimen.size_200dp)
            val bitmapTask: Deferred<Bitmap?> = async(Dispatchers.Default) {
                generateQRBitmap(qrInput.getInputData(), size = Size(size, size))
            }

            bitmapQr = bitmapTask.await()
            hideLoading()
            if (bitmapQr != null) {
                binding.imgQr.setImageBitmap(bitmapQr)
            } else {
                showSingleOptionDialog(
                    title = "Lỗi",
                    message = "Lỗi rồi thử lại",
                    button = "Đóng",
                    listener = {
                        onBackPressed()
                    }
                )
            }
        }
    }

    private fun generateQRBitmap(value: String, size: Size): Bitmap? {
        return QRUtils.generateQRBitmap(value, size)
    }
}