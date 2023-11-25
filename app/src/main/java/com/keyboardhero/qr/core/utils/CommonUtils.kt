package com.keyboardhero.qr.core.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.widget.Toast
import com.keyboardhero.qr.Constant
import com.keyboardhero.qr.NetworkConfig
import com.keyboardhero.qr.core.utils.logging.DebugLog
import java.text.DecimalFormat
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

    /**
     * This method using to format account number or phone number, separat by " "
     *
     * @param accountOrPhoneNumber
     * @param firstIndex first position to add devider " "
     * @param secondIndex second position to add devider " "
     * @param thirdIndex third position to add devider " "
     */
    fun formatAccountOrPhoneNumber(
        accountOrPhoneNumber: String,
        firstIndex: Int,
        secondIndex: Int,
        thirdIndex: Int,
    ): String {
        val devider = " "
        val formatted = StringBuilder()
        var index = 0
        for (character in accountOrPhoneNumber) {
            formatted.append(character)
            index++
            if (index == firstIndex || index == secondIndex || index == thirdIndex) {
                formatted.append(devider)
                index++
            }
        }
        return formatted.toString()
    }

    /**
     * This method using to format money in the form ***,***,***
     *
     * @param money
     */
    fun formatMoney(money: String): String {
        val decimalFormat = DecimalFormat("#,###,###,###")
        val value = clearCurrencyToNumber(money)
        return decimalFormat.format(value?.toDouble()).replace(".", ",")
    }

    private fun clearCurrencyToNumber(currencyValue: String?): String? {
        return currencyValue?.replace(Constant.SUFFIX_US, "")
            ?.replace("[(a-z)|(A-Z)|($,. )]".toRegex(), "")
            ?: ""
    }
}
