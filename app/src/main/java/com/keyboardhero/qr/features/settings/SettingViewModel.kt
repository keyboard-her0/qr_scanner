package com.keyboardhero.qr.features.settings

import androidx.lifecycle.viewModelScope
import com.keyboardhero.qr.core.base.BaseViewModel
import com.keyboardhero.qr.core.utils.CommonUtils
import com.keyboardhero.qr.shared.domain.dto.SettingsConfig
import com.keyboardhero.qr.shared.domain.dto.ThemeSetting
import com.keyboardhero.qr.shared.domain.usecase.GetSettingsConfigUseCase
import com.keyboardhero.qr.shared.domain.usecase.SaveSettingsConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val saveSettingsConfigUseCase: SaveSettingsConfigUseCase,
    private val getSettingsConfigUseCase: GetSettingsConfigUseCase
) : BaseViewModel<SettingsViewState, SettingsViewEvents>() {
    override fun initState(): SettingsViewState =
        SettingsViewState(loading = false, themes = emptyList(), SettingsConfig())

    fun initData() {
        viewModelScope.launch {
            dispatchState(currentState.copy(loading = true))
            val result = getSettingsConfigUseCase.invoke(Unit).getOrNull()

            val typeThemeSelected = ThemeSetting.ThemeType.getValue(result?.typeTheme)

            val themes = listOf(
                ThemeSetting.Theme(
                    title = "Auto",
                    resIconId = 0,
                    type = ThemeSetting.ThemeType.AUTO,
                    isSelected = ThemeSetting.ThemeType.AUTO == typeThemeSelected
                ),
                ThemeSetting.Theme(
                    title = "Day",
                    resIconId = 0,
                    type = ThemeSetting.ThemeType.DAY,
                    isSelected = ThemeSetting.ThemeType.DAY == typeThemeSelected
                ),
                ThemeSetting.Theme(
                    title = "Night",
                    resIconId = 0,
                    type = ThemeSetting.ThemeType.NIGHT,
                    isSelected = ThemeSetting.ThemeType.NIGHT == typeThemeSelected
                )
            )
            dispatchState(
                currentState.copy(
                    themes = themes,
                    loading = false,
                    settingsConfig = result ?: SettingsConfig(
                        typeThemeSelected.value, vibration = false, sound = false
                    )
                )
            )
        }
    }

    fun setVibration(enable: Boolean) {
        viewModelScope.launch {
            saveSettingsConfigUseCase.invoke(currentState.settingsConfig.copy(vibration = enable))
        }
    }

    fun setSound(enable: Boolean) {
        viewModelScope.launch {
            saveSettingsConfigUseCase.invoke(currentState.settingsConfig.copy(sound = enable))
        }
    }

    fun changeTheme(theme: ThemeSetting.Theme) {
        viewModelScope.launch {
            dispatchState(currentState.copy(loading = true))
            val themes = currentState.themes.map {
                it.copy(isSelected = it.type.value == theme.type.value)
            }
            CommonUtils.switchThemeMode(theme.type.value)
            saveTheme(theme)
            dispatchState(
                currentState.copy(loading = false, themes = themes)
            )
        }
    }

    private suspend fun saveTheme(theme: ThemeSetting.Theme) {
        saveSettingsConfigUseCase.invoke(currentState.settingsConfig.copy(typeTheme = theme.type.value))
    }
}