package com.keyboardhero.qr.features.create

data class CreateViewState(
    val loading: Boolean,
    val generateItem: List<CreateTypeItem>
)