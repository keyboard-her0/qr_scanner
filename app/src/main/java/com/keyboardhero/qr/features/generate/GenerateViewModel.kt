package com.keyboardhero.qr.features.generate

import com.keyboardhero.qr.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel @Inject constructor() :
    BaseViewModel<GenerateViewState, GenerateViewEvents>() {
    override fun initState(): GenerateViewState = GenerateViewState(false)
}