package com.keyboardhero.qr.core.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import com.keyboardhero.qr.BuildConfig
import com.keyboardhero.qr.core.utils.logging.DebugLog
import com.keyboardhero.qr.shared.domain.dto.ThemeSetting
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object CommonUtils {
    /**
     * This method using to open web browser
     *
     * @param context [Context]
     * @param url url
     */
    @SuppressLint("QueryPermissionsNeeded")
    fun openWebPage(context: Context, url: String?) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    /**
     * This method using to call to a phone number
     *
     * @param context [Context]
     * @param phoneNumber phone number
     */
    @SuppressLint("QueryPermissionsNeeded")
    fun openPhoneCall(context: Context, phoneNumber: String?) {
        val number: Uri = Uri.parse(phoneNumber)
        val intent = Intent(Intent.ACTION_CALL, number)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    /**
     * This method using to convert Base64 string to bitmap
     *
     * @param base64String base64 string
     */
    fun convertBase64StringToBitmap(base64String: String): Bitmap {
        var base64Image = base64String
        if (base64String.split(",").toTypedArray().size > 1) {
            base64Image = base64String.split(",").toTypedArray()[1]
        }
        val bitmapData = Base64.decode(base64Image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.size)
    }

    /**
     * This method using to get time now and format time
     *
     * @param format format you want
     */
    fun getTimeNow(format: String = "HH:mm  -  dd/MM/yyyy"): String {
        val result: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern(format)
            current.format(formatter)
        } else {
            val date = Date()
            val formatter = SimpleDateFormat(format, Locale.getDefault())
            formatter.format(date)
        }
        return result
    }

    fun isGestureNavigationEnabled(contentResolver: ContentResolver): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Settings.Secure.getInt(contentResolver, "navigation_mode", 0) == 2
        } else {
            false
        }
    }

    fun createIntentShareStringData(value: String, title: String): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, value)
        return Intent.createChooser(intent, title)
    }

    fun createIntentShareUriData(uri: Uri, title: String): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        return Intent.createChooser(intent, title)
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun getStatusBarHeight(context: Context): Int {
        val statusBarHeightId = context.resources.getIdentifier(
            "status_bar_height", "dimen", "android"
        )
        return context.resources.getDimensionPixelSize(statusBarHeightId)
    }

    fun switchThemeMode(mode: Int) {
        when (mode) {
            ThemeSetting.ThemeType.AUTO.ordinal -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }

            ThemeSetting.ThemeType.DAY.ordinal -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateToString(
        timestamp: Long,
        dateFormat: String = "HH:mm  -  dd/MM/yyyy",
    ): String {
        return runCatching {
            val sdf = SimpleDateFormat(dateFormat)
            val date = Date(timestamp)
            sdf.format(date)
        }.getOrElse { error ->
            DebugLog.w(error.message ?: "convertDateTo $dateFormat failed")
            ""
        }
    }

    suspend fun saveImage(bitmap: Bitmap, context: Context): Boolean {
        return suspendCoroutine { continuation ->
            val resolver = context.contentResolver
            val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageName = "QRCode_$timeStamp.jpg"
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, imageName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.WIDTH, bitmap.width)
                put(MediaStore.Images.Media.HEIGHT, bitmap.height)
            }
            try {
                resolver.insert(imageCollection, contentValues)
                    ?.also { resultUri ->
                        resolver.openOutputStream(resultUri)?.use { outputStream ->
                            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                                throw IOException("Unable to save bitmap")
                            }
                        }
                    } ?: throw IOException("Unable to create MediaStore entry")
                continuation.resume(true)
            } catch (e: IOException) {
                e.printStackTrace()
                continuation.resume(false)
            }
        }

    }

    fun vibrate(context: Context, timeInMillis: Long = 200L) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(timeInMillis, 125))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(timeInMillis)
        }
    }

    fun openUriInApp(context: Context, uri: Uri) {
        val stringUri = uri.toString()
        val newUri = if (!stringUri.startsWith("https://") && !stringUri.startsWith("http://")) {
            Uri.parse("https://$stringUri")
        } else uri
        val defaultColors = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(
                getColorFromAttr(
                    context,
                    com.google.android.material.R.attr.colorPrimaryVariant
                )
            )
            .setNavigationBarColor(
                getColorFromAttr(
                    context,
                    com.google.android.material.R.attr.colorPrimaryVariant
                )
            )
            .build()

        val builder = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setDefaultColorSchemeParams(defaultColors)

        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, newUri)
    }

    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    fun getNavigationBarHeight(context: Context): Int {
        val resources: Resources = context.resources
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics =
                context.getSystemService(WindowManager::class.java).currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars())
            insets.bottom
        } else {
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                resources.getDimensionPixelSize(resourceId)
            } else {
                0
            }
        }
    }

    fun getAppVersion(context: Context): String {
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return if (BuildConfig.DEBUG) {
                pInfo.versionName + " - ${BuildConfig.BUILD_TYPE}"
            } else pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "--"
    }
}
