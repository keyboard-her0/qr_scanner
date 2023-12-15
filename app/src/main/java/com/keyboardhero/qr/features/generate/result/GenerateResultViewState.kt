package com.keyboardhero.qr.features.generate.result

import com.keyboardhero.qr.shared.domain.dto.input.BarcodeData

data class GenerateResultViewState(
    val loading: Boolean,
    val qrInput: BarcodeData?
)