package com.keyboardhero.qr.shared.domain.dto

import android.graphics.Bitmap
import com.keyboardhero.qr.R

sealed class Action(
    val actionNameResId: Int,
    val iconRes: Int
) {
    data class Copy(val value: String) : Action(actionNameResId = R.string.copy, R.drawable.ic_copy)
    data class Share(val value: String) :
        Action(actionNameResId = R.string.share, R.drawable.ic_share)

    data class ShareImage(val bitmap: Bitmap) :
        Action(actionNameResId = R.string.share, R.drawable.ic_share)

    data class Save(val bitmap: Bitmap) :
        Action(actionNameResId = R.string.save, R.drawable.ic_save)

    data class Search(val keySearch: String) :
        Action(actionNameResId = R.string.search, R.drawable.ic_search)

//    data class Connect(val ssid: String, val password: String) :
//        Action(actionNameResId = R.string.connect, R.drawable.ic_wifi)

    data class Call(val phoneNumber: String) :
        Action(actionNameResId = R.string.call, R.drawable.ic_call)

    data class SendEmail(val email: String, val subject: String, val message: String) :
        Action(actionNameResId = R.string.send_email, R.drawable.ic_send)

    data class SendSms(val phoneNumber: String, val message: String) :
        Action(actionNameResId = R.string.send_sms, R.drawable.ic_send)

    data class Open(val url: String) :
        Action(actionNameResId = R.string.open, R.drawable.ic_browser)
}