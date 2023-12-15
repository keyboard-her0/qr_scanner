package com.keyboardhero.qr.features.scan

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.core.utils.logging.DebugLog
import com.keyboardhero.qr.databinding.FragmentScannerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanFragment : BaseFragment<FragmentScannerBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScannerBinding
        get() = FragmentScannerBinding::inflate

    private val viewModel: ScanViewModel by viewModels()
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var selectPictureContract: ActivityResultLauncher<String>

    override fun initData(data: Bundle?) {
        selectPictureContract =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    val bitmap = getBitmapFromUri(uri)
                    val value = scanFromPatch(bitmap)
                    if (value != null) {
                        shareData(value)
                    } else {
                        Toast.makeText(requireContext(), "Lỗi", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    override fun initViews() {
        if (isDeviceSupport()) {
            initCamera()
            binding.previewView.scaleType = PreviewView.ScaleType.FILL_CENTER
        } else {
            showSingleOptionDialog(
                title = "Lỗi",
                message = "Thiết bị của bạn không được hỗ trợ camera",
                button = "OK"
            )
        }
    }

    private fun initCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProvider?.unbindAll()
        cameraProviderFuture.addListener(
            {
                try {
                    cameraProvider = cameraProviderFuture.get()
                    processScan()
                } catch (e: Exception) {
                    DebugLog.e("initCamera Error: ${e.printStackTrace()}")
                }
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processScan() {
        val preview = Preview.Builder()
            .build()

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
            .build()

        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(requireContext())
        ) { imageProxy ->
            val image = InputImage.fromMediaImage(
                imageProxy.image!!, imageProxy.imageInfo.rotationDegrees
            )

            val options = BarcodeScannerOptions.Builder().build()
            val scanner = BarcodeScanning.getClient(options)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        val rect = barcodes.getOrNull(0)?.boundingBox
                        if (rect != null) {
                            binding.barcodePreview.rectDetection(
                                rect,
                                Size(imageProxy.width, imageProxy.height)
                            )
                        }
                    } else {
                        binding.barcodePreview.start()
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        requireContext(),
                        "Failed to scan.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnCompleteListener(
                    ContextCompat.getMainExecutor(requireContext())
                ) { imageProxy.close() }
        }

        preview.setSurfaceProvider(binding.previewView.surfaceProvider)

        val useCaseGroup = UseCaseGroup.Builder()
            .addUseCase(preview)
            .addUseCase(imageAnalysis)
            .build()

        camera = cameraProvider?.bindToLifecycle(
            viewLifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            useCaseGroup
        )
    }

    private fun enableFlash(enable: Boolean, cameraId: String) {

    }

    override fun initHeaderAppBar() {
        headerAppBar.isVisible = false
    }

    private fun isDeviceSupport(): Boolean {
        return requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    private fun shareData(value: String) {

    }

    override fun initActions() {
        binding.btnSelectQR.setOnClickListener {
            requestPermissions(READ_EXTERNAL_STORAGE) { isGranted, _ ->
                if (isGranted) {
                    selectPictureContract.launch(IMAGE_FILTER)
                } else {
                    showSingleOptionDialog(
                        title = "Lỗi",
                        message = "Không có quyền truy cập thư viện",
                        button = "Đóng"
                    )
                }
            }
        }

        binding.btnToggleFlash.setOnClickListener {
            binding.barcodePreview.start()
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }

    private fun scanFromPatch(bitmap: Bitmap): String? {
//        try {
//            val frame = Frame.Builder().setBitmap(bitmap).build()
//
//            val barcodes = barcodeDetector.detect(frame)
//            if (barcodes.size() > 0) {
//                return barcodes.valueAt(0).displayValue
//            }
//        } catch (e: Exception) {
//            e.stackTrace
//        }
        return null
    }


    override fun initObservers() {
        //Do nothing
    }

    companion object {
        private const val IMAGE_FILTER = "image/*"
        private const val CAMERA_PREVIEW_WIDTH = 1920
        private const val CAMERA_PREVIEW_HEIGHT = 1080
        private const val CAMERA_PREVIEW_FPS = 35F
    }
}