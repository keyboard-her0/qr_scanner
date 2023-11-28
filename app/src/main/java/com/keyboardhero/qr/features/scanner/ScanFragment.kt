package com.keyboardhero.qr.features.scanner

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.databinding.FragmentScannerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanFragment : BaseFragment<FragmentScannerBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScannerBinding
        get() = FragmentScannerBinding::inflate

    private val viewModel: ScanViewModel by viewModels()
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var selectPictureContract: ActivityResultLauncher<String>
    private var isScanning = true

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
        initCameraSource()
        openCameraView()
    }

    private fun initCameraSource() {
        barcodeDetector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.QR_CODE).build()

        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(CAMERA_PREVIEW_WIDTH, CAMERA_PREVIEW_HEIGHT)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedFps(CAMERA_PREVIEW_FPS)
            .setAutoFocusEnabled(true)
            .build()
    }

    private fun openCameraView() {
        binding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(p0: SurfaceHolder) {
                startCameraSource()
                detectQRCode()
            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
                //Do nothing
            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                cameraSource.stop()
            }
        })
    }

    private fun startCameraSource() {
        requestPermissions(CAMERA) { isGranted, _ ->
            if (isGranted) {
                cameraSource.start(binding.surfaceView.holder)
            } else {
                onBackPressed()
            }
        }
    }

    private fun detectQRCode() {
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                //Do nothing
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() > 0) {
                    val barcode = barcodes.valueAt(0)

                    binding.surfaceView.rectDetection(
                        barcode.boundingBox,
                        Size(CAMERA_PREVIEW_WIDTH, CAMERA_PREVIEW_HEIGHT)
                    )

                    if (isScanning) {
                        shareData(barcode.displayValue)
                    }
                } else {
                    binding.surfaceView.setDefault()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        isScanning = false
    }

    override fun onResume() {
        super.onResume()
        isScanning = true
    }

    private fun shareData(value: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = INTENT_TYPE
        intent.putExtra(Intent.EXTRA_TEXT, value)

        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(Intent.createChooser(intent, "Chia sẻ với"))
        }
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
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }

    private fun scanFromPatch(bitmap: Bitmap): String? {
        try {
            val frame = Frame.Builder().setBitmap(bitmap).build()

            val barcodes = barcodeDetector.detect(frame)
            if (barcodes.size() > 0) {
                return barcodes.valueAt(0).displayValue
            }
        } catch (e: Exception) {
            e.stackTrace
        }
        return null
    }


    override fun initObservers() {
        //Do nothing
    }

    companion object {
        private const val IMAGE_FILTER = "image/*"
        private const val INTENT_TYPE = "text/plain"
        private const val CAMERA_PREVIEW_WIDTH = 1920
        private const val CAMERA_PREVIEW_HEIGHT = 1080
        private const val CAMERA_PREVIEW_FPS = 35F
    }
}