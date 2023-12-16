package com.keyboardhero.qr.shared.domain.dto.barcodedata

import kotlinx.parcelize.Parcelize

@Parcelize
data class SmsBarcode(
    val phoneNumber: String,
    val message: String,
) : BarcodeData {
    override fun getInputData(): String {
       return message
    }
}