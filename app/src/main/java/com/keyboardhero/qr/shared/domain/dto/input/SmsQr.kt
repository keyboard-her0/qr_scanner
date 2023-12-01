package com.keyboardhero.qr.shared.domain.dto.input

import kotlinx.parcelize.Parcelize

@Parcelize
data class SmsQr(
    val phoneNumber: String,
    val message: String,
    override val isFavorite: Boolean = false
) : QrObject {
    override fun getInputData(): String {
       return message
    }
}