package com.keyboardhero.qr.shared.domain.dto.input

import android.os.Parcelable

interface QrObject : Parcelable {
    fun getInputData(): String
    val isFavorite: Boolean
}