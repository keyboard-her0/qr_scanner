package com.keyboardhero.qr.features.settings.language

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.keyboardhero.qr.core.utils.logging.DebugLog
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
    private fun setAppLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res = context.resources
        val config = Configuration(res.configuration)
        config.setLocale(locale)
        res.updateConfiguration(config, res.displayMetrics)
    }

    fun applyLocale(
        activity: Activity?,
        languageCode: String,
        recreateActivity: Boolean = true
    ) {
        if (activity == null) {
            DebugLog.w("Activity is null")
            return
        }

        val locale = Locale(languageCode)
        // Validate inputted language code
        val validatedLocale = Locale.getAvailableLocales().firstOrNull {
            it.language == locale.language
        } ?: return
        val baseContext = activity.baseContext

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val baseLocale = baseContext.resources.configuration.locales.get(0)
            if (!baseLocale.toString().equals(validatedLocale.toString(), ignoreCase = true)) {
                Locale.setDefault(validatedLocale)
                val config = getLocalizedConfiguration(validatedLocale)
                baseContext.resources.updateConfiguration(
                    config,
                    baseContext.resources.displayMetrics
                )
                if (recreateActivity) {
                    ActivityCompat.recreate(activity)
                }
            }
        } else {
            setAppLocale(baseContext, languageCode)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getLocalizedConfiguration(locale: Locale): Configuration {
        val config = Configuration()
        return config.apply {
            config.setLayoutDirection(locale)
            config.setLocale(locale)
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            config.setLocales(localeList)
        }
    }
}