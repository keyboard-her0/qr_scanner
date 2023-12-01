package com.keyboardhero.qr.features.generate.result

import com.keyboardhero.qr.shared.domain.dto.input.QrObject

data class GenerateResultViewState(
    val loading: Boolean,
    val qrInput: QrObject?
)