package com.keyboardhero.qr.features.settings

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.viewModelScope
import com.keyboardhero.qr.core.base.BaseViewModel
import com.keyboardhero.qr.core.utils.CommonUtils
import com.keyboardhero.qr.shared.domain.dto.ThemeSetting
import com.keyboardhero.qr.shared.domain.usecase.GetThemeSavedUseCase
import com.keyboardhero.qr.shared.domain.usecase.SaveThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val saveThemeUseCase: SaveThemeUseCase,
    private val getThemeSavedUseCase: GetThemeSavedUseCase
) : BaseViewModel<SettingsViewState, SettingsViewEvents>() {
    override fun initState(): SettingsViewState =
        SettingsViewState(loading = false, themes = emptyList())

    fun initThemes() {
        viewModelScope.launch {
            dispatchState(currentState.copy(loading = true))
            val typeThemeSelected = getThemeSaved()

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
            dispatchState(currentState.copy(themes = themes, loading = false))
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

    private suspend fun getThemeSaved(): ThemeSetting.ThemeType {
        val result = getThemeSavedUseCase.invoke(Unit)
        return if (result.isSuccess) {
            ThemeSetting.ThemeType.getValue(result.getOrNull())
        } else {
            ThemeSetting.DEFAULT_THEME_MODE
        }
    }

    private suspend fun saveTheme(theme: ThemeSetting.Theme) {
        saveThemeUseCase.invoke(theme.type.value)
    }
}