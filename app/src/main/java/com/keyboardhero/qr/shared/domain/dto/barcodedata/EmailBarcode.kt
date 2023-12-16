package com.keyboardhero.qr.shared.domain.dto.barcodedata

import com.keyboardhero.qr.shared.domain.dto.barcodedata.BarcodeData
import kotlinx.parcelize.Parcelize

@Parcelize
data class EmailBarcode(
    val email: String,
    val subject: String,
    val message: String,
) : BarcodeData {
    override fun getInputData(): String {
        return ""
    }
}