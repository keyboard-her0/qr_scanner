package com.keyboardhero.qr.shared.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.keyboardhero.qr.shared.data.db.entity.HistoryEntity


@Dao
interface HistoryDao {

    @Query("SELECT * FROM History")
    fun getAllHistory(): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(historyEntity: HistoryEntity)
    @Delete
    fun deleteHistory(historyEntity: HistoryEntity)

    @Update
    fun updateHistory(historyEntity: HistoryEntity)
}