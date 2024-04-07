package com.keyboardhero.qr.features.create.result

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.keyboardhero.qr.core.base.BaseViewModel
import com.keyboardhero.qr.core.utils.CommonUtils
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import com.keyboardhero.qr.shared.domain.dto.HistoryDTO
import com.keyboardhero.qr.shared.domain.dto.barcodedata.BarcodeData
import com.keyboardhero.qr.shared.domain.usecase.SaveHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateResultViewModel @Inject constructor(
    private val saveHistoryUseCase: SaveHistoryUseCase

) : BaseViewModel<GenerateResultViewState, GenerateResultViewEvents>() {
    override fun initState(): GenerateResultViewState = GenerateResultViewState(false, null)

    fun setup(type: BarcodeType, barcodeData: BarcodeData, isCreateNew: Boolean) {
        viewModelScope.launch {
            dispatchState(currentState.copy(loading = true))
            val historyDTO = HistoryDTO(
                id = 0,
                isScan = false,
                createAt = CommonUtils.getTimeNow(),
                barcodeType = type,
                barcodeData = barcodeData
            )
            if (isCreateNew) {
                saveHistory(historyDTO)
            }
            renderIntoBitmap(
                MultiFormatWriter().encode(
                    barcodeData.getInputData(), BarcodeFormat.QR_CODE, 800, 800
                )
            )
            dispatchState(currentState.copy(barcodeData = barcodeData, loading = false))
        }
    }

    private fun renderIntoBitmap(bitMatrix: BitMatrix) {
        viewModelScope.launch {
            val width = bitMatrix.width
            val height = bitMatrix.height
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                }
            }
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
                setPixels(pixels, 0, width, 0, 0, width, height)
            }

            dispatchState(currentState.copy(bitmap = bitmap))
        }
    }


    private suspend fun saveHistory(historyDTO: HistoryDTO) {
        saveHistoryUseCase.invoke(historyDTO)
    }
}