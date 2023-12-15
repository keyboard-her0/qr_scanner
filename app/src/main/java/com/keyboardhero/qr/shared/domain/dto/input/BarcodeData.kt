package com.keyboardhero.qr.shared.domain.dto.input

import android.os.Parcelable
import com.keyboardhero.qr.core.utils.JsonUtility.toJson

interface BarcodeData : Parcelable {
    fun getInputData(): String
    val isFavorite: Boolean
}