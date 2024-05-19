package com.keyboardhero.qr.shared.domain.dto.barcodedata

import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationBarcode(
    private val latitude: Long,
    private val longitude: Long,
    private val request: String = ""
) : BarcodeData {
    override fun getInputData(): String {
        return if (request.isNotEmpty()) {
            "geo:$latitude,$longitude?q=$request"
        } else {
            "geo:$latitude,$longitude"
        }
    }
}