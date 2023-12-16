package com.keyboardhero.qr.shared.domain.dto.barcodedata

import kotlinx.parcelize.Parcelize

@Parcelize
data class TextBarcode(
    val value: String,
) : BarcodeData {
    override fun getInputData(): String {
        return value
    }
}
