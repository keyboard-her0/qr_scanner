package com.keyboardhero.qr.shared.domain.dto

import android.os.Parcelable
import com.keyboardhero.qr.shared.domain.dto.barcodedata.BarcodeData
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryDTO(
    val id: Long,
    val isScan: Boolean,
    val createAt: String,
    val barcodeData: BarcodeData,
    val barcodeType: BarcodeType,
): Parcelable