package com.keyboardhero.qr.shared.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.keyboardhero.qr.shared.data.db.dao.HistoryDao
import com.keyboardhero.qr.shared.data.db.entity.HistoryEntity

@Database(
    entities = [HistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}