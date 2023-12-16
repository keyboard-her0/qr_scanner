package com.keyboardhero.qr.shared.domain.dto.barcodedata

import kotlinx.parcelize.Parcelize

@Parcelize
data class UrlBarcode(
    val url: String,
) : BarcodeData {
    override fun getInputData(): String {
        return url
    }
}