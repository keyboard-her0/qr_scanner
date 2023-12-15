package com.keyboardhero.qr.shared.domain.dto.input

import kotlinx.parcelize.Parcelize

@Parcelize
data class TextBarcode(
    val data: String,
    override val isFavorite: Boolean = false
) : BarcodeData {
    override fun getInputData(): String {
        return data
    }
}
