package com.keyboardhero.qr.shared.data.repositoryImpl

import com.keyboardhero.qr.shared.data.datasource.HistoryDataSource
import com.keyboardhero.qr.shared.data.toHistoryDTO
import com.keyboardhero.qr.shared.data.toHistoryEntity
import com.keyboardhero.qr.shared.domain.dto.HistoryDTO
import com.keyboardhero.qr.shared.domain.repository.HistoryRepository
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDataSource: HistoryDataSource
) : HistoryRepository {
    override fun getAllHistory(): List<HistoryDTO> {
        return historyDataSource.getAllHistory().map { it.toHistoryDTO() }
    }

    override fun updateHistory(history: HistoryDTO) {
        historyDataSource.updateHistory(history = history.toHistoryEntity())
    }

    override fun addHistory(history: HistoryDTO) {
        historyDataSource.insertHistory(history = history.toHistoryEntity())
    }

    override fun deleteHistory(history: HistoryDTO) {
        historyDataSource.deleteHistory(history = history.toHistoryEntity())
    }
}