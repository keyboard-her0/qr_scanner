package com.keyboardhero.qr.shared.domain.dto.barcodedata

import kotlinx.parcelize.Parcelize

@Parcelize
data class WifiBarcode(
    val ssid: String,
    val password: String,
    val isHide: Boolean,
    val type: TypeSecurity,
) : BarcodeData {
    override fun getInputData(): String {
        return "WIFI:S:$ssid;T:$type;P:$password;H:$isHide;"
    }

    enum class TypeSecurity(val typeName: String) {
        WPA2("WPA2"),
        WPA("WPA"),
        WEP("WEP"),
        NONE("None");
    }
}

