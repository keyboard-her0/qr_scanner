package com.keyboardhero.qr.features.scanner

import com.keyboardhero.qr.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor() : BaseViewModel<ScanViewStates, ScanViewEvents>() {
    override fun initState(): ScanViewStates = ScanViewStates(false, "")
}