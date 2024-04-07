package com.keyboardhero.qr.shared.domain.dto.barcodedata

import kotlinx.parcelize.Parcelize

@Parcelize
data class EmailBarcode(
    val email: String,
    val subject: String,
    val message: String,
) : BarcodeData {
    override fun getInputData(): String {
        return "MATMSG:TO:$email;SUB:$subject;BODY:$message"
    }
}