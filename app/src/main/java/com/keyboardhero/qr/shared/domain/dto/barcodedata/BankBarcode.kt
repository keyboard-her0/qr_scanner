package com.keyboardhero.qr.shared.domain.dto.barcodedata

import kotlinx.parcelize.Parcelize

@Parcelize
data class BankBarcode(
    val bankNumber: String,
    val amount: String,
    val message: String,
) : BarcodeData {
    override fun getInputData(): String {
        return ""
    }
}

