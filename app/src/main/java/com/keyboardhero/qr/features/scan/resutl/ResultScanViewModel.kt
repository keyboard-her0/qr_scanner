package com.keyboardhero.qr.features.scan.resutl

import com.keyboardhero.qr.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultScanViewModel @Inject constructor(): BaseViewModel<ResultScanViewState, ResultScanViewEvents>() {
    override fun initState(): ResultScanViewState = ResultScanViewState()

    fun iniData(data: String) {
        dispatchState(currentState.copy(barcodeData = data))
    }
}