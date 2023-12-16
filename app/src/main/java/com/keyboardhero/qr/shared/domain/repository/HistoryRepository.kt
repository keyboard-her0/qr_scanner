package com.keyboardhero.qr.shared.domain.repository

import com.keyboardhero.qr.shared.domain.dto.HistoryDTO

interface HistoryRepository {
    fun getAllHistory(): List<HistoryDTO>

    fun updateHistory(history: HistoryDTO)

    fun addHistory(history: HistoryDTO)

    fun deleteHistory(history: HistoryDTO)
}