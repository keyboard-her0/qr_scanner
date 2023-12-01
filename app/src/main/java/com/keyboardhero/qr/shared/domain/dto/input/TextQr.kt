package com.keyboardhero.qr.shared.domain.dto.input

import kotlinx.android.parcel.Parcelize

@Parcelize
data class TextQr(
    val data: String,
    override val isFavorite: Boolean = false
) : QrObject {
    override fun getInputData(): String {
        return data
    }
}
