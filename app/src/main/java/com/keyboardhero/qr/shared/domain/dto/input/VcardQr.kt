package com.keyboardhero.qr.shared.domain.dto.input

import kotlinx.android.parcel.Parcelize

@Parcelize
data class VcardQr(
    val firstName: String,
    val lastName: String,
    val mobile: String,
    val phone: String,
    val fax: String,
    val email: String,
    val company: String,
    val job: String,
    val street: String,
    val city: String,
    val zip: String,
    val state: String,
    val country: String,
    val website: String,
    override val isFavorite: Boolean = false
) : QrObject {
    override fun getInputData(): String {
        return ""
    }
}
