package com.keyboardhero.qr.features.generate.result

import com.keyboardhero.qr.core.base.BaseViewModel
import com.keyboardhero.qr.shared.domain.dto.input.QrObject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GenerateResultViewModel @Inject constructor() :
    BaseViewModel<GenerateResultViewState, GenerateResultViewEvents>() {
    override fun initState(): GenerateResultViewState = GenerateResultViewState(false, null)

    fun setData(qrInput: QrObject) {
        dispatchState(currentState.copy(qrInput = qrInput))
    }
}