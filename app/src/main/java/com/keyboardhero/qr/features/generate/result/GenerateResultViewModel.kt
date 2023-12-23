package com.keyboardhero.qr.features.generate.result

import androidx.lifecycle.viewModelScope
import com.keyboardhero.qr.core.base.BaseViewModel
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

    fun setup(type: BarcodeType, barcodeData: BarcodeData) {
        viewModelScope.launch {
            dispatchState(currentState.copy(loading = true))
            val historyDTO = HistoryDTO(
                id = 0,
                favorite = false,
                createAt = System.currentTimeMillis(),
                barcodeType = type,
                barcodeData = barcodeData
            )
            saveHistory(historyDTO)
            dispatchState(currentState.copy(barcodeData = barcodeData, loading = false))
        }
    }

    private suspend fun saveHistory(historyDTO: HistoryDTO) {
        saveHistoryUseCase.invoke(historyDTO)
    }
}