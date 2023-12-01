package com.keyboardhero.qr.shared.domain.dto.input

import kotlinx.android.parcel.Parcelize

@Parcelize
data class EmailQr(
    val email: String,
    val subject: String,
    val message: String,
    override val isFavorite: Boolean = false
) : QrObject {
    override fun getInputData(): String {
        return ""
    }
}