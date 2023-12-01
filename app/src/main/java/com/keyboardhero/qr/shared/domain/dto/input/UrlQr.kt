package com.keyboardhero.qr.shared.domain.dto.input

import kotlinx.parcelize.Parcelize

@Parcelize
data class UrlQr(
    val url: String,
    override val isFavorite: Boolean = false
) : QrObject {
    override fun getInputData(): String {
        return url
    }
}