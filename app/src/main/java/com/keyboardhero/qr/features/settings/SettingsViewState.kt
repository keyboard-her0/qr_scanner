package com.keyboardhero.qr.features.settings

import com.keyboardhero.qr.shared.domain.dto.ThemeSetting

data class SettingsViewState(
    val loading: Boolean,
    val themes : List<ThemeSetting.Theme>
)
