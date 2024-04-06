package com.keyboardhero.qr.features.create

import com.keyboardhero.qr.shared.domain.dto.BarcodeType

data class CreateViewState(
    val loading: Boolean,
    val generateItem: List<BarcodeType>
)