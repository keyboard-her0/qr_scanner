package com.keyboardhero.qr.features.history

import androidx.lifecycle.viewModelScope
import com.keyboardhero.qr.core.base.BaseViewModel
import com.keyboardhero.qr.shared.domain.dto.HistoryDTO
import com.keyboardhero.qr.shared.domain.usecase.EditHistoryUseCase
import com.keyboardhero.qr.shared.domain.usecase.GetAllHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val editHistoryUseCase: EditHistoryUseCase,
    private val getAllHistoryUseCase: GetAllHistoryUseCase
) :
    BaseViewModel<HistoryViewState, HistoryViewEvents>() {
    override fun initState(): HistoryViewState = HistoryViewState(false, emptyList())

    fun favoriteHistory(history: HistoryDTO) {
        viewModelScope.launch {
            val result = editHistoryUseCase.invoke(history.copy(favorite = !history.favorite))
            result.getOrNull()?.let {
                dispatchState(currentState.copy(listHistory = it))
            }
        }
    }

    fun getAllHistory() {
        viewModelScope.launch {
            dispatchState(currentState.copy(loading = true))
            val result = getAllHistoryUseCase.invoke(Unit)
            dispatchState(
                currentState.copy(
                    loading = false,
                    listHistory = result.getOrNull() ?: emptyList()
                )
            )
        }
    }
}