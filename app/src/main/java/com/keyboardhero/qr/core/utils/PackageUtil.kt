package com.keyboardhero.qr.core.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.keyboardhero.qr.BuildConfig
import com.keyboardhero.qr.Constant
import com.keyboardhero.qr.core.utils.logging.DebugLog
import java.util.UUID

object PackageUtil {

    /**
     * This method using to get version code of Application
     *
     * @param context [Context]
     * return version app
     */
    fun getVersionApp(context: Context): String {
        return try {
            val pInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            DebugLog.e(e.message.toString())
            Constant.EMPTY_STRING
        }
    }

    /**
     * This method using to get device id
     *
     * return device id
     */
    fun getDeviceId(): String {
        return UUID.randomUUID().toString()
    }

    /**
     * This method using to app version code
     *
     * return version code
     */
    fun getAppVersionCode(): String {
        return BuildConfig.VERSION_CODE.toString()
    }

    /**
     * This method using to platform app
     *
     * return device platform
     */
    fun getPlatformVersion(): String {
        return "1.0"
    }

    fun getDeviceName(): String {
        val manufacturer: String = Build.MANUFACTURER
        val model: String = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else {
            capitalize(manufacturer) + " " + model
        }
    }

    private fun capitalize(characters: String?): String {
        if (characters.isNullOrEmpty()) {
            return Constant.EMPTY_STRING
        }
        val first = characters[0]
        return if (Character.isUpperCase(first)) {
            characters
        } else {
            Character.toUpperCase(first).toString() + characters.substring(1)
        }
    }
}
