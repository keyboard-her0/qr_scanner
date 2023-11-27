package com.keyboardhero.qr.features.scanner

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.databinding.FragmentScannerBinding
import java.io.File


class ScannerFragment : BaseFragment<FragmentScannerBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScannerBinding
        get() = FragmentScannerBinding::inflate

    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private val sizeDetection = Size(1920, 1080)
    override fun initData(data: Bundle?) {

    }

    override fun initViews() {
        initCameraSource()
        openCameraView()
    }

    private fun initCameraSource() {
        barcodeDetector =
            BarcodeDetector.Builder(requireContext()).setBarcodeFormats(Barcode.QR_CODE).build()

        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(sizeDetection.width, sizeDetection.height)
            .setFacing(CameraSource.CAMERA_FACING_BACK).setRequestedFps(35f)
            .setAutoFocusEnabled(true).build()
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
        requestPermissions(CAMERA) { isAllow, _ ->
            if (isAllow) {
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

                val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val display = windowManager.defaultDisplay
                val rotation = display.rotation
                Log.d("AAA", "receiveDetections: $rotation")

                if (barcodes.size() > 0) {
                    Log.d("AAA", "receiveDetections: ${cameraSource.previewSize}")
                    val barcode = barcodes.valueAt(0)
                    val rect = barcode.boundingBox
                    binding.surfaceView.rectDetection(rect, sizeDetection)
                } else {
                    binding.surfaceView.setDefault()
                }
            }
        })
    }

    override fun initActions() {
        binding.btnSelectQR.setOnClickListener {
            requestPermissions(READ_EXTERNAL_STORAGE) { isAllow, _ ->
                if (isAllow) {
                    chooseImage()
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

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val data = data.data
            Toast.makeText(requireContext(), "Image selected! $data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun scanFromPatch(path: String): String? {
        try {
            val file = File(path)
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)

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

    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 12551
    }
}