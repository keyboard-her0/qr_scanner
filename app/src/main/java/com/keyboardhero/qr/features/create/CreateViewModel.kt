package com.keyboardhero.qr.features.create

import com.keyboardhero.qr.core.base.BaseViewModel
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor() :
    BaseViewModel<CreateViewState, CreateViewEvents>() {
    override fun initState(): CreateViewState = CreateViewState(false, generateItems)

    private val generateItems = BarcodeType.values()

}