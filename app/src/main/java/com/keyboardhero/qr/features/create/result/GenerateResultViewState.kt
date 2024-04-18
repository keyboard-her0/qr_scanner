package com.keyboardhero.qr.features.create.result

import android.graphics.Bitmap
import com.keyboardhero.qr.shared.domain.dto.Action
import com.keyboardhero.qr.shared.domain.dto.barcodedata.BarcodeData

data class GenerateResultViewState(
    val loading: Boolean,
    val barcodeData: BarcodeData?,
    val bitmap: Bitmap? = null,
    val actions: List<Action> = emptyList(),
)