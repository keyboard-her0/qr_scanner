package com.keyboardhero.qr.shared.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "History")
data class HistoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "Id")
    val id: Long,

    @ColumnInfo(name = "favorite")
    val favorite: Boolean,

    @ColumnInfo(name = "createAt")
    val createAt: Long,

    @ColumnInfo(name = "jsonData")
    val jsonData: String,

    @ColumnInfo(name = "type")
    val type: Int
)