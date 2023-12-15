package com.keyboardhero.qr.shared.domain.dto.input

import kotlinx.parcelize.Parcelize

@Parcelize
data class WifiBarcode(
    val ssid: String,
    val password: String,
    val isHide: Boolean,
    val type: TypeSecurity,
    override val isFavorite: Boolean = false
) : BarcodeData {
    override fun getInputData(): String {
        return ""
    }

    enum class TypeSecurity {
        NONE,
        WPA,
        WEP
    }
}

