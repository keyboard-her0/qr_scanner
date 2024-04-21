package com.keyboardhero.qr

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.keyboardhero.qr.core.utils.CommonUtils
import com.keyboardhero.qr.features.settings.language.LanguageManager
import com.keyboardhero.qr.shared.data.AppPreference
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class QRApplication : Application() {

    @Inject
    lateinit var appPreference: AppPreference
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(QRActivityLifecycleCallbacks(appPreference))
    }
}

private class QRActivityLifecycleCallbacks(
    private val appPreference: AppPreference
) : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

        with(appPreference.languageCode) {
            LanguageManager.applyLocale(
                activity = activity,
                languageCode = this
            )
        }

        with(appPreference.theme) {
            CommonUtils.switchThemeMode(this)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        // suppress
    }

    override fun onActivityResumed(activity: Activity) {
        // suppress
    }

    override fun onActivityPaused(activity: Activity) {
        // suppress
    }

    override fun onActivityStopped(activity: Activity) {
        // suppress
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // suppress
    }

    override fun onActivityDestroyed(activity: Activity) {
        // suppress
    }
}