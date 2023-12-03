package com.keyboardhero.qr.shared.domain.usecase

import com.keyboardhero.qr.di.IoDispatcher
import com.keyboardhero.qr.shared.data.AppPreference
import com.keyboardhero.qr.shared.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetThemeSavedUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val appPreference: AppPreference
) : UseCase<Unit, Int>(dispatcher) {
    override suspend fun execute(parameters: Unit): Int {
        return appPreference.theme
    }
}