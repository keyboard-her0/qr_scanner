package com.keyboardhero.qr.shared.domain.usecase

import com.keyboardhero.qr.di.IoDispatcher
import com.keyboardhero.qr.shared.data.AppPreference
import com.keyboardhero.qr.shared.domain.UseCase
import com.keyboardhero.qr.shared.domain.dto.SettingsConfig
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetSettingsConfigUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val appPreference: AppPreference
) : UseCase<Unit, SettingsConfig>(dispatcher) {
    override suspend fun execute(parameters: Unit): SettingsConfig {
        return SettingsConfig(
            appPreference.theme,
            appPreference.vibration,
            appPreference.sound
        )
    }
}