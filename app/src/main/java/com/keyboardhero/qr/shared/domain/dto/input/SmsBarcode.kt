package com.keyboardhero.qr.shared.domain.dto.input

import kotlinx.parcelize.Parcelize

@Parcelize
data class SmsBarcode(
    val phoneNumber: String,
    val message: String,
    override val isFavorite: Boolean = false
) : BarcodeData {
    override fun getInputData(): String {
       return message
    }
}