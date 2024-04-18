package com.keyboardhero.qr.shared.domain.usecase

import com.keyboardhero.qr.di.IoDispatcher
import com.keyboardhero.qr.shared.data.AppPreference
import com.keyboardhero.qr.shared.domain.UseCase
import com.keyboardhero.qr.shared.domain.dto.SettingsConfig
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SaveSettingsConfigUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val appPreference: AppPreference
) : UseCase<SettingsConfig, Unit>(dispatcher) {
    override suspend fun execute(parameters: SettingsConfig) {
        appPreference.theme = parameters.typeTheme
        appPreference.vibration = parameters.vibration
        appPreference.sound = parameters.sound
    }
}