package com.keyboardhero.qr.features.generate.result

import android.util.Size
import androidx.lifecycle.viewModelScope
import com.keyboardhero.qr.core.base.BaseViewModel
import com.keyboardhero.qr.core.utils.qr.QRUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateResultViewModel @Inject constructor() :
    BaseViewModel<GenerateResultViewState, GenerateResultViewEvents>() {
    override fun initState(): GenerateResultViewState = GenerateResultViewState(false, null)

    fun generateQrFromString(value: String) {
        viewModelScope.launch {
            dispatchState(currentState.copy(loading = true))
            val result = QRUtils.generateQRBitmap(
                value = value,
                size = Size(500, 500)
            )

            if (result != null) {
                dispatchEvent(GenerateResultViewEvents.GenerateQRSuccess(result))
            } else {
                dispatchEvent(GenerateResultViewEvents.GenerateQRFail)
            }
            dispatchState(currentState.copy(loading = false, bitmapQR = result))
        }
    }
}