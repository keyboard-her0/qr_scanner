package com.keyboardhero.qr.features.scan

import com.keyboardhero.qr.core.base.BaseViewModel
import com.keyboardhero.qr.shared.data.AppPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val appPreference: AppPreference
) : BaseViewModel<ScanViewStates, ScanViewEvents>() {
    override fun initState(): ScanViewStates = ScanViewStates()

    fun setHasFrontCamera(value: Boolean) {
        dispatchState(currentState.copy(hasFrontCamera = value))
    }

    fun setHasFlashUnit(value: Boolean) {
        dispatchState(currentState.copy(hasFlashUnit = value))
    }

    fun setFlashEnable(value: Boolean) {
        dispatchState(currentState.copy(flashEnable = value))
    }

    fun setIsBackCamera(value: Boolean) {
        dispatchState(currentState.copy(isBackCamera = value))
    }

    init {
        dispatchState(currentState.copy(allowVibration = appPreference.vibration))
    }
}