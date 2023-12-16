package com.keyboardhero.qr.shared.domain.dto.barcodedata

import android.os.Parcelable

interface BarcodeData : Parcelable {
    fun getInputData(): String
}