package com.keyboardhero.qr.shared.domain.dto.input

import kotlinx.parcelize.Parcelize

@Parcelize
data class UrlBarcode(
    val url: String,
    override val isFavorite: Boolean = false
) : BarcodeData {
    override fun getInputData(): String {
        return url
    }
}