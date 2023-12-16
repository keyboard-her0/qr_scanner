package com.keyboardhero.qr.features.history

import androidx.lifecycle.viewModelScope
import com.keyboardhero.qr.core.base.BaseViewModel
import com.keyboardhero.qr.shared.domain.dto.HistoryDTO
import com.keyboardhero.qr.shared.domain.usecase.GetAllHistoryUseCase
import com.keyboardhero.qr.shared.domain.usecase.SaveHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val saveHistoryUseCase: SaveHistoryUseCase,
    private val getAllHistoryUseCase: GetAllHistoryUseCase
) :
    BaseViewModel<HistoryViewState, HistoryViewEvents>() {
    override fun initState(): HistoryViewState = HistoryViewState(false, emptyList())

    fun saveHistory(historyDTO: HistoryDTO){
        viewModelScope.launch {
            saveHistoryUseCase.invoke(historyDTO)
        }
    }

    fun getAllHistory(){
        viewModelScope.launch {
            dispatchState(currentState.copy(loading = true))
            val result = getAllHistoryUseCase.invoke(Unit)
            dispatchState(currentState.copy(loading = false, listHistory = result.getOrNull() ?: emptyList()))
        }
    }
}