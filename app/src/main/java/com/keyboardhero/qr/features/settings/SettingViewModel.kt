package com.keyboardhero.qr.features.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.viewModelScope
import com.keyboardhero.qr.core.base.BaseViewModel
import com.keyboardhero.qr.shared.domain.dto.ThemeSetting
import kotlinx.coroutines.launch

class SettingViewModel : BaseViewModel<SettingsViewState, SettingsViewEvents>() {
    override fun initState(): SettingsViewState = SettingsViewState(false)

    fun switchThemeMode(mode: Int) {
        viewModelScope.launch {
            when (mode) {
                ThemeSetting.ThemeType.AUTO.ordinal -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }

                ThemeSetting.ThemeType.DAY.ordinal -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }
    }
}