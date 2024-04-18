package com.keyboardhero.qr.features.scan

data class ScanViewStates(
    val flashEnable: Boolean = false,
    val isBackCamera: Boolean = true,
    val hasFrontCamera: Boolean = false,
    val hasFlashUnit: Boolean = false,
    val allowVibration: Boolean = false,
)