package com.keyboardhero.qr.core.utils

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.InputType
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.keyboardhero.qr.NetworkConfig
import com.keyboardhero.qr.core.utils.logging.DebugLog
import com.keyboardhero.qr.shared.domain.dto.ThemeSetting
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object CommonUtils {
    /**
     * This method using to open web browser
     *
     * @param context [Context]
     * @param url url
     */
    fun openWebPage(context: Context, url: String?) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    /**
     * This method using to open messenger chat to an user
     *
     * @param context [Context]
     * @param user user to chat with
     */
    fun openAppMessenger(context: Context, user: String?) {
        val uri: Uri = Uri.parse(user)
        val intent = Intent(Intent.ACTION_VIEW, uri)

        try {
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            DebugLog.e("Messenger is not installed")
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(NetworkConfig.MESSENGER_APP_URI),
                ),
            )
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
     * This method using to show a toast message
     *
     * @param context [Context]
     * @param message message
     */
    fun openToastDialog(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
    fun getTimeNow(format: String): String {
        val result: String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern(format)
            result = current.format(formatter)
        } else {
            val date = Date()
            val formatter = SimpleDateFormat(format, Locale.getDefault())
            result = formatter.format(date)
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

    @SuppressLint("InternalInsetResource")
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

}
