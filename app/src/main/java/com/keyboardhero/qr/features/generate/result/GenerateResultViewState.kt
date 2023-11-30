package com.keyboardhero.qr.features.generate.result

import android.graphics.Bitmap

data class GenerateResultViewState(
    val loading: Boolean,
    val bitmapQR: Bitmap?
)