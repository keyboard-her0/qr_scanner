package com.keyboardhero.qr.shared.domain.dto

data class SettingsConfig(
    val typeTheme: Int = ThemeSetting.DEFAULT_THEME_MODE.value,
    val vibration: Boolean = false,
    val sound: Boolean = false,
)
