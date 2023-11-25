package com.keyboardhero.qr.core.utils.language

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LanguageManager {

    object LocaleName {
        const val ENGLISH = "en"
        const val VIETNAM = "vi"
    }

    /**
     * This method using to update app resources by locale name
     *
     * @param context [Context]
     * @param language locale name
     */
    fun setAppLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res = context.resources
        val config = Configuration(res.configuration)
        config.setLocale(locale)
        res.updateConfiguration(config, res.displayMetrics)
    }
}
