package com.keyboardhero.qr.shared.data

import com.google.gson.Gson
import com.keyboardhero.qr.core.utils.JsonUtility.toJson
import com.keyboardhero.qr.shared.data.db.entity.HistoryEntity
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import com.keyboardhero.qr.shared.domain.dto.HistoryDTO
import com.keyboardhero.qr.shared.domain.dto.barcodedata.BarcodeData

fun HistoryDTO.toHistoryEntity(): HistoryEntity {
    return HistoryEntity(
        id = this.id,
        isScan = this.isScan,
        createAt = this.createAt,
        jsonData = this.barcodeData.toJson(),
        type = this.barcodeType.typeId
    )
}

fun HistoryEntity.toHistoryDTO(): HistoryDTO {
    val barcodeType = BarcodeType.getTypeFormId(this.type)
    return HistoryDTO(
        id = this.id,
        isScan = this.isScan,
        createAt = this.createAt,
        barcodeType = barcodeType,
        barcodeData = Gson().fromJson(this.jsonData, barcodeType.datatype) as BarcodeData,
    )
}