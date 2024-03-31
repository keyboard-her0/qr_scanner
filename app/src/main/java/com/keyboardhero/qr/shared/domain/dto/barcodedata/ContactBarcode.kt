package com.keyboardhero.qr.shared.domain.dto.barcodedata

import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactBarcode(
    val name: String,
    val phoneNumber: String,
) : BarcodeData {
    override fun getInputData(): String {
        return "smsto:$phoneNumber:$name"
    }
}