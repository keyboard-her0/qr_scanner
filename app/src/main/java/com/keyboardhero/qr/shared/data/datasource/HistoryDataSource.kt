package com.keyboardhero.qr.shared.data.datasource

import android.util.Log
import com.keyboardhero.qr.shared.data.db.dao.HistoryDao
import com.keyboardhero.qr.shared.data.db.entity.HistoryEntity
import javax.inject.Inject
class HistoryDataSource @Inject constructor(
    private val historyDao: HistoryDao
) {
    fun getAllHistory(): List<HistoryEntity> {
        return historyDao.getAllHistory()
    }

    fun insertHistory(history: HistoryEntity) {
        historyDao.insertHistory(history)
    }

    fun updateHistory(history: HistoryEntity) {
        historyDao.updateHistory(history)
    }

    fun deleteHistory(history: HistoryEntity) {
        historyDao.deleteHistory(history)
    }
}