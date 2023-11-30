package com.keyboardhero.qr.features.generate.result

import android.graphics.Bitmap

sealed interface GenerateResultViewEvents{
    data class GenerateQRSuccess(val bitmap: Bitmap) : GenerateResultViewEvents
    object GenerateQRFail: GenerateResultViewEvents
}