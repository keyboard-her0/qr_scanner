package com.keyboardhero.qr.core.utils.qr

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Size
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.keyboardhero.qr.core.utils.logging.DebugLog

object QRUtils {

    fun generateQRBitmap(value: String, size: Size): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val hintMap = mapOf(EncodeHintType.MARGIN to 2)

            val bitMatrix = QRCodeWriter().encode(
                value, BarcodeFormat.QR_CODE, size.width, size.height, hintMap
            )

            bitmap = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888)

            for (x in 0 until size.width) {
                for (y in 0 until size.height) {
                    bitmap.setPixel(x, y, getColorWithPixel(bitMatrix = bitMatrix, x = x, y = y))
                }
            }

        } catch (e: Exception) {
            DebugLog.e("GenerateQRBitmap error: ${e.printStackTrace()}")
        }
        return bitmap
    }

    private fun getColorWithPixel(bitMatrix: BitMatrix, x: Int, y: Int) = if (bitMatrix[x, y]) {
        Color.BLACK
    } else {
        Color.WHITE
    }
}