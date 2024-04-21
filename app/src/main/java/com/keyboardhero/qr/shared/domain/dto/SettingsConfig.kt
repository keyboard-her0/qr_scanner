package com.keyboardhero.qr.shared.domain.dto

import com.keyboardhero.qr.features.settings.language.LanguageManager

data class SettingsConfig(
    val typeTheme: Int = ThemeSetting.DEFAULT_THEME_MODE.value,
    val languageCode: String = LanguageManager.LocaleName.ENGLISH,
    val vibration: Boolean = false,
    val sound: Boolean = false,
)
