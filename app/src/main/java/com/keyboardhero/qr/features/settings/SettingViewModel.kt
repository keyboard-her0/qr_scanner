package com.keyboardhero.qr.features.settings

import androidx.lifecycle.viewModelScope
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseViewModel
import com.keyboardhero.qr.core.utils.CommonUtils
import com.keyboardhero.qr.features.settings.language.LanguageManager
import com.keyboardhero.qr.shared.domain.dto.Language
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
    private val getSettingsConfigUseCase: GetSettingsConfigUseCase,
) : BaseViewModel<SettingsViewState, SettingsViewEvents>() {
    override fun initState(): SettingsViewState =
        SettingsViewState(
            loading = false,
            themes = emptyList(),
            languages = emptyList(),
            settingsConfig = SettingsConfig()
        )

    fun initData() {
        viewModelScope.launch {
            dispatchState(currentState.copy(loading = true))
            val result = getSettingsConfigUseCase.invoke(Unit).getOrNull()

            val typeThemeSelected = ThemeSetting.ThemeType.getValue(result?.typeTheme)

            val languages = listOf(
                Language(
                    code = LanguageManager.LocaleName.ENGLISH,
                    nameResId = R.string.language_eng,
                    isSelected = LanguageManager.LocaleName.ENGLISH == result?.languageCode
                ),
                Language(
                    code = LanguageManager.LocaleName.VIETNAM,
                    nameResId = R.string.language_vi,
                    isSelected = LanguageManager.LocaleName.VIETNAM == result?.languageCode
                )
            )

            val themes = listOf(
                ThemeSetting.Theme(
                    titleResId = R.string.theme_auto,
                    resIconId = 0,
                    type = ThemeSetting.ThemeType.AUTO,
                    isSelected = ThemeSetting.ThemeType.AUTO == typeThemeSelected
                ),
                ThemeSetting.Theme(
                    titleResId = R.string.theme_day,
                    resIconId = 0,
                    type = ThemeSetting.ThemeType.DAY,
                    isSelected = ThemeSetting.ThemeType.DAY == typeThemeSelected
                ),
                ThemeSetting.Theme(
                    titleResId = R.string.theme_night,
                    resIconId = 0,
                    type = ThemeSetting.ThemeType.NIGHT,
                    isSelected = ThemeSetting.ThemeType.NIGHT == typeThemeSelected
                )
            )
            dispatchState(
                currentState.copy(
                    themes = themes,
                    loading = false,
                    languages = languages,
                    settingsConfig = result ?: SettingsConfig(
                        typeThemeSelected.value,
                        vibration = false,
                        sound = false,
                        languageCode = LanguageManager.LocaleName.ENGLISH
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

    fun changeLanguage(language: Language) {
        viewModelScope.launch {
            dispatchState(currentState.copy(loading = true))
            val languages = currentState.languages.map {
                it.copy(isSelected = it.code == language.code)
            }
            saveLanguage(language)
            dispatchState(
                currentState.copy(loading = false, languages = languages)
            )
        }
    }

    private suspend fun saveLanguage(language: Language) {
        saveSettingsConfigUseCase.invoke(currentState.settingsConfig.copy(languageCode = language.code))
    }
}