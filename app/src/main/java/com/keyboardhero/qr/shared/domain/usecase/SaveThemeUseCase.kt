package com.keyboardhero.qr.shared.domain.usecase

import com.keyboardhero.qr.di.IoDispatcher
import com.keyboardhero.qr.shared.data.AppPreference
import com.keyboardhero.qr.shared.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SaveThemeUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val appPreference: AppPreference
) : UseCase<Int, Unit>(dispatcher) {
    override suspend fun execute(parameters: Int) {
        appPreference.theme = parameters
    }
}