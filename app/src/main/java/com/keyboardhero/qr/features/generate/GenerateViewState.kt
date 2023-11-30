package com.keyboardhero.qr.features.generate

data class GenerateViewState(
    val loading: Boolean,
    val generateItem: List<GenerateItem>
)