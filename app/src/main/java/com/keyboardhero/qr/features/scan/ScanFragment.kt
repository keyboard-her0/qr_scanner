package com.keyboardhero.qr.features.scan

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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

    private lateinit var cameraSelector: CameraSelector
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private var processCameraProvider: ProcessCameraProvider? = null
    private lateinit var imageAnalysis: ImageAnalysis
    private lateinit var selectPictureContract: ActivityResultLauncher<String>

    override fun initData(data: Bundle?) {
        selectPictureContract =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    val bitmap = getBitmapFromUri(uri)
                    val value = scanFromPatch(bitmap)
                    if (value != null) {
                        //shareData(value)
                    } else {
                        Toast.makeText(requireContext(), "Lỗi", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    override fun initViews() {

    }

    private fun initCamera() {
        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        processCameraProvider?.unbindAll()
        cameraProviderFuture.addListener(
            {
                try {
                    processCameraProvider = cameraProviderFuture.get()
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
            .setTargetRotation(binding.previewView.display.rotation)
            .build()

        preview.setSurfaceProvider(binding.previewView.surfaceProvider)

        imageAnalysis = ImageAnalysis.Builder()
            .setTargetRotation(binding.previewView.display.rotation)
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
                        stopScanAnimation()
                        val barcode = barcodes.getOrNull(0)

                    } else {
                        startScanAnimation()
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

        try {
            processCameraProvider?.bindToLifecycle(
                viewLifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
        } catch (illegalStateException: IllegalStateException) {
            DebugLog.e("Unhandled exception $illegalStateException.message")
        } catch (illegalArgumentException: IllegalArgumentException) {
            DebugLog.e("Unhandled exception $illegalArgumentException.message")
        }

    }

    override fun initHeaderAppBar() {
        headerAppBar.isVisible = false
    }

    private fun startScanAnimation() {
        binding.barcodePreview.isScanAnimation = true
    }

    private fun stopScanAnimation() {
        binding.barcodePreview.isScanAnimation = false
    }

    private fun isDeviceSupport(): Boolean {
        return requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    override fun initActions() {
        with(binding) {
            imgBack.setOnClickListener {
                onBackPressed()
            }

            btnSelectPhoto.setOnClickListener {
                val permissionImage = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
                    READ_MEDIA_IMAGES
                } else {
                    READ_EXTERNAL_STORAGE
                }

                requestPermissions(permissionImage) { isGranted, _ ->
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

            btnToggleFlash.setOnClickListener {
            }
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


    override fun onResume() {
        super.onResume()
        if (isDeviceSupport()) {
            requestPermissions(CAMERA) { isGranted, _ ->
                if (isGranted) {
                    initCamera()
                } else {
                    showSingleOptionDialog(
                        title = "Lỗi",
                        message = "Không có quyền truy cập máy ảnh",
                        button = "Đóng"
                    )
                }
            }
        } else {
            showSingleOptionDialog(
                title = "Lỗi",
                message = "Thiết bị của bạn không được hỗ trợ camera",
                button = "OK"
            )
        }
        startScanAnimation()
    }

    override fun onPause() {
        super.onPause()
        stopScanAnimation()
    }

    override fun initObservers() {
        //Do nothing
    }

    override fun onDestroyView() {
        processCameraProvider?.unbindAll()
        super.onDestroyView()
    }

    companion object {
        private const val IMAGE_FILTER = "image/*"
        private const val CAMERA_PREVIEW_WIDTH = 1920
        private const val CAMERA_PREVIEW_HEIGHT = 1080
        private const val CAMERA_PREVIEW_FPS = 35F
    }
}