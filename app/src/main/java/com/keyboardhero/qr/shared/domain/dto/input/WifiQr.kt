package com.keyboardhero.qr.shared.domain.dto.input

import kotlinx.android.parcel.Parcelize

@Parcelize
data class WifiQr(
    val ssid: String,
    val password: String,
    val isHide: Boolean,
    val type: TypeSecurity,
    override val isFavorite: Boolean = false
) : QrObject {
    override fun getInputData(): String {
        return ""
    }

    enum class TypeSecurity {
        NONE,
        WPA,
        WEP
    }
}

