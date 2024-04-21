package com.keyboardhero.qr.features.settings

import com.keyboardhero.qr.shared.domain.dto.Language
import com.keyboardhero.qr.shared.domain.dto.SettingsConfig
import com.keyboardhero.qr.shared.domain.dto.ThemeSetting

data class SettingsViewState(
    val loading: Boolean,
    val themes: List<ThemeSetting.Theme>,
    val languages: List<Language>,
    val settingsConfig: SettingsConfig
)
