package com.keyboardhero.qr.features.scan

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.core.utils.logging.DebugLog
import com.keyboardhero.qr.databinding.FragmentScannerBinding
import com.keyboardhero.qr.features.scan.resutl.ResultScanFragmentArgs
import com.keyboardhero.qr.features.scan.resutl.ResultScanScreen
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class ScanFragment : BaseFragment<FragmentScannerBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScannerBinding
        get() = FragmentScannerBinding::inflate

    private val viewModel: ScanViewModel by viewModels()

    private lateinit var cameraSelector: CameraSelector
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var preview: Preview
    private lateinit var imageAnalysis: ImageAnalysis
    private lateinit var selectPictureContract: ActivityResultLauncher<String>

    private var processCameraProvider: ProcessCameraProvider? = null
    private var camera: Camera? = null

    override fun initData(data: Bundle?) {
        selectPictureContract =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    scanFromPatch(requireContext(), uri) { barcodes ->
                        if (!barcodes.isNullOrEmpty()) {
                            handleScanResult(barcodes)
                        } else {
                            showSingleOptionDialog(
                                title = "Lỗi",
                                message = "Không có thể tìm thấy nội dung từ ảnh",
                                button = "Đóng"
                            )
                        }
                    }
                }
            }
    }

    override fun initViews() {
        //Do nothing
    }

    @OptIn(ExperimentalGetImage::class)
    private fun initCamera() {
        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview = Preview.Builder()
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
                        handleScanResult(barcodes)
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

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                try {
                    processCameraProvider?.unbindAll()
                    processCameraProvider = cameraProviderFuture.get()
                    processScan()
                } catch (e: Exception) {
                    DebugLog.e("initCamera Error: ${e.printStackTrace()}")
                }
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    private fun handleScanResult(barcodes: List<Barcode>) {
        val barcode = barcodes.getOrNull(0)?.rawValue
        if (barcode?.isNotBlank() == true) {
            navigateToResultScreen(barcode)
        }
    }

    private fun navigateToResultScreen(barcode: String) {
        router.navigate(
            ResultScanScreen,
            ResultScanFragmentArgs(
                scanData = barcode
            ).toBundle()
        )
    }

    private fun processScan() {
        try {
            processCameraProvider?.unbindAll()
            camera = processCameraProvider?.bindToLifecycle(
                viewLifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )

            val hasFlashUnit = camera?.cameraInfo?.hasFlashUnit()
            val hasFrontCamera = processCameraProvider?.hasCamera(
                CameraSelector.DEFAULT_FRONT_CAMERA
            )
            viewModel.setHasFlashUnit(hasFlashUnit ?: false)
            viewModel.setHasFrontCamera(hasFrontCamera ?: false)
            viewModel.setFlashEnable(false)
            viewModel.setIsBackCamera(cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)

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
                handleActionFlash()
            }

            imgSwitchCamera.setOnClickListener {
                handleSwitchCamera()
            }
        }
    }

    private fun handleActionFlash() {
        if (viewModel.currentState.hasFlashUnit) {
            if (!viewModel.currentState.flashEnable) {
                camera?.cameraControl?.enableTorch(true)
                viewModel.setFlashEnable(true)
            } else {
                camera?.cameraControl?.enableTorch(false)
                viewModel.setFlashEnable(false)
            }
        } else {
            showSingleOptionDialog(
                title = "Lỗi",
                message = "Thiết bị không hỗ trợ đèn flash",
                button = "Đóng"
            )
        }
    }

    private fun handleSwitchCamera() {
        if (viewModel.currentState.hasFrontCamera) {
            if (viewModel.currentState.isBackCamera) {
                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                viewModel.setIsBackCamera(false)
            } else {
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                viewModel.setIsBackCamera(true)
            }
            processScan()
        } else {
            showSingleOptionDialog(
                title = "Lỗi",
                message = "Thiết bị của bạn không được hỗ trợ camera trước",
                button = "OK"
            )
        }
    }

    private fun scanFromPatch(
        context: Context,
        imageUri: Uri,
        result: (List<Barcode>?) -> Unit
    ) {
        val options = BarcodeScannerOptions.Builder().build()
        val scanner = BarcodeScanning.getClient(options)
        try {
            val image = InputImage.fromFilePath(context, imageUri)
            scanner.process(image).addOnSuccessListener { barcodes: List<Barcode> ->
                result(barcodes)
            }.addOnFailureListener { e: Exception ->
                result(null)
                e.printStackTrace()
            }
        } catch (e: IOException) {
            result(null)
            e.printStackTrace()
        }
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
        viewModel.observe(
            owner = viewLifecycleOwner,
            selector = { state -> state.flashEnable },
            observer = { flashEnable ->
                binding.btnToggleFlash.setImageResource(
                    if (flashEnable) R.drawable.ic_flash_on else R.drawable.ic_flash_off
                )
            }
        )
    }

    override fun onDestroyView() {
        processCameraProvider?.unbindAll()
        super.onDestroyView()
    }

    companion object {
        private const val IMAGE_FILTER = "image/*"
    }
}