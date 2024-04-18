package com.keyboardhero.qr.shared.data

import android.content.SharedPreferences
import com.keyboardhero.qr.core.utils.sharedpreference.BooleanPreferenceDelegate
import com.keyboardhero.qr.core.utils.sharedpreference.IntPreferenceDelegate
import com.keyboardhero.qr.di.SecurePreference
import com.keyboardhero.qr.shared.domain.dto.ThemeSetting
import javax.inject.Inject

interface AppPreference {
    var theme: Int
    var vibration: Boolean
    var sound: Boolean
}

class AppPreferenceImpl @Inject constructor(
    @SecurePreference val encryptedPrefs: SharedPreferences,
) : AppPreference {

    override var theme: Int by IntPreferenceDelegate(
        encryptedPrefs,
        THEME_KEY,
        ThemeSetting.DEFAULT_THEME_MODE.value
    )
    override var vibration: Boolean by BooleanPreferenceDelegate(
        encryptedPrefs, VIBRATION_KEY, false
    )
    override var sound: Boolean by BooleanPreferenceDelegate(
        encryptedPrefs, SOUND_KEY, false
    )


    companion object {
        private const val THEME_KEY = "THEME_KEY"
        private const val VIBRATION_KEY = "VIBRATION_KEY"
        private const val SOUND_KEY = "SOUND_KEY"
    }
}