package com.keyboardhero.qr.shared.domain.dto.input

import kotlinx.parcelize.Parcelize

@Parcelize
data class EmailBarcode(
    val email: String,
    val subject: String,
    val message: String,
    override val isFavorite: Boolean = false
) : BarcodeData {
    override fun getInputData(): String {
        return ""
    }
}