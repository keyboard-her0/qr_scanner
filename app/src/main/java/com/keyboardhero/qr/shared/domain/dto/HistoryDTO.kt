package com.keyboardhero.qr.shared.domain.dto

import com.keyboardhero.qr.shared.domain.dto.barcodedata.BarcodeData

data class HistoryDTO(
    val id: Long,
    val favorite: Boolean,
    val createAt: Long,
    val barcodeData: BarcodeData,
    val barcodeType: BarcodeType,
)