package com.keyboardhero.qr.shared.domain.dto.barcodedata

import kotlinx.parcelize.Parcelize

@Parcelize
data class AztecBarcode(
    private val input: String
) : BarcodeData {
    override fun getInputData(): String {
        return input
    }
}
