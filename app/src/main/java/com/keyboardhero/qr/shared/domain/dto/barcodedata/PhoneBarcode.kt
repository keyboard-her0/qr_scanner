package com.keyboardhero.qr.shared.domain.dto.barcodedata

import kotlinx.parcelize.Parcelize

@Parcelize
data class PhoneBarcode(val phoneNumber: String) : BarcodeData {
    override fun getInputData(): String {
        return "tel:$phoneNumber"
    }
}
