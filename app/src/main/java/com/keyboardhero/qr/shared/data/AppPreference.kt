package com.keyboardhero.qr.shared.data

import android.content.SharedPreferences
import com.keyboardhero.qr.core.utils.sharedpreference.IntPreferenceDelegate
import com.keyboardhero.qr.di.SecurePreference
import com.keyboardhero.qr.shared.domain.dto.ThemeSetting
import javax.inject.Inject

interface AppPreference {
    var theme: Int
}

class AppPreferenceImpl @Inject constructor(
    @SecurePreference val encryptedPrefs: SharedPreferences,
) : AppPreference {

    override var theme: Int by IntPreferenceDelegate(
        encryptedPrefs,
        THEME_KEY,
        ThemeSetting.DEFAULT_THEME_MODE.value
    )


    companion object {
        private const val THEME_KEY = "THEME_KEY"
    }
}