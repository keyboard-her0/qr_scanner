package com.keyboardhero.qr.shared.domain.usecase

import com.keyboardhero.qr.di.IoDispatcher
import com.keyboardhero.qr.shared.domain.UseCase
import com.keyboardhero.qr.shared.domain.dto.HistoryDTO
import com.keyboardhero.qr.shared.domain.repository.HistoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SaveHistoryUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val historyRepository: HistoryRepository
) : UseCase<HistoryDTO, Unit>(dispatcher) {
    override suspend fun execute(parameters: HistoryDTO) {
        historyRepository.addHistory(parameters)
    }
}
