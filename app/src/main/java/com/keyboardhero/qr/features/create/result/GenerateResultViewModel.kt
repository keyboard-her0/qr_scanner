package com.keyboardhero.qr.features.create.result

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.keyboardhero.qr.core.base.BaseViewModel
import com.keyboardhero.qr.core.utils.CommonUtils
import com.keyboardhero.qr.shared.domain.dto.Action
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import com.keyboardhero.qr.shared.domain.dto.HistoryDTO
import com.keyboardhero.qr.shared.domain.dto.barcodedata.BarcodeData
import com.keyboardhero.qr.shared.domain.usecase.SaveHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateResultViewModel @Inject constructor(
    private val saveHistoryUseCase: SaveHistoryUseCase,
    private val multiFormatWriter: MultiFormatWriter
) : BaseViewModel<GenerateResultViewState, GenerateResultViewEvents>() {
    override fun initState(): GenerateResultViewState = GenerateResultViewState(false, null)

    fun setup(type: BarcodeType, barcodeData: BarcodeData, isCreateNew: Boolean) {
        viewModelScope.launch {
            dispatchState(currentState.copy(loading = true))
            if (isCreateNew) {
                val historyDTO = HistoryDTO(
                    id = 0,
                    isScan = false,
                    createAt = CommonUtils.getTimeNow(),
                    barcodeType = type,
                    barcodeData = barcodeData
                )
                saveHistory(historyDTO)
            }
            val properties = BarcodeImageProperties(
                contents = barcodeData.getInputData(),
                format = type.barcodeFormat
            )
            renderIntoBitmap(properties)
            dispatchState(currentState.copy(barcodeData = barcodeData, loading = false))
        }
    }


    private fun renderIntoBitmap(properties: BarcodeImageProperties) {
        viewModelScope.launch {
            val bitmap = Bitmap.createBitmap(
                properties.width, properties.height, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            val paint = Paint().apply {
                color = properties.backgroundColor
                isAntiAlias = true
            }

            //Create Background
            canvas.drawRect(
                0f,
                0f,
                properties.width.toFloat(),
                properties.height.toFloat(),
                paint
            )

            //Create Content
            val matrix = encodeBarcodeImage(
                properties.contents,
                properties.format,
                properties.hints
            )

            val unitW: Float = properties.width / matrix.width.toFloat()
            val unitH: Float = properties.height / matrix.height.toFloat()
            val cornerRadius = unitW / 2f * properties.cornerRadius

            paint.apply {
                color = properties.frontColor
                isAntiAlias = cornerRadius != 0f
            }

            for (x in 0 until matrix.width) {
                for (y in 0 until matrix.height) {
                    if (matrix[x, y]) {
                        val left = x.toFloat() * unitW
                        val top = y.toFloat() * unitH
                        val right = left + unitW
                        val bottom = top + unitH

                        canvas.drawRoundRect(
                            left, top, right, bottom, cornerRadius, cornerRadius, paint
                        )
                    }
                }
            }
            dispatchState(
                currentState.copy(
                    bitmap = bitmap,
                    actions = listOf(
                        Action.ShareImage(bitmap = bitmap),
                        Action.Save(bitmap = bitmap)
                    )
                )
            )
        }
    }

    private fun encodeBarcodeImage(
        text: String,
        barcodeFormat: BarcodeFormat,
        hints: Map<EncodeHintType, Any>
    ): BitMatrix {
        return multiFormatWriter.encode(text, barcodeFormat, 0, 0, hints)
    }

    private suspend fun saveHistory(historyDTO: HistoryDTO) {
        saveHistoryUseCase.invoke(historyDTO)
    }

    data class BarcodeImageProperties(
        val contents: String,
        val format: BarcodeFormat,
        val qrCodeErrorCorrectionLevel: ErrorCorrectionLevel? = null,
        val width: Int = BARCODE_IMAGE_DEFAULT_SIZE,
        val height: Int = BARCODE_IMAGE_DEFAULT_SIZE,
        val cornerRadius: Float = 0f,
        @ColorInt var frontColor: Int = Color.BLACK,
        @ColorInt var backgroundColor: Int = Color.WHITE
    ) {
        val hints: Map<EncodeHintType, Any>
            get() {
                val encoding: String = when (format) {
                    BarcodeFormat.QR_CODE, BarcodeFormat.PDF_417 -> "UTF-8"
                    else -> "ISO-8859-1"
                }

                return qrCodeErrorCorrectionLevel?.let {
                    mapOf<EncodeHintType, Any>(
                        EncodeHintType.CHARACTER_SET to encoding,
                        EncodeHintType.ERROR_CORRECTION to it
                    )
                } ?: run {
                    mapOf<EncodeHintType, Any>(EncodeHintType.CHARACTER_SET to encoding)
                }
            }
    }

    companion object {
        private const val BARCODE_IMAGE_DEFAULT_SIZE = 800
    }
}