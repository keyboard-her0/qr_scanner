package com.keyboardhero.qr.shared.domain.dto

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.keyboardhero.qr.R
import com.keyboardhero.qr.shared.domain.dto.barcodedata.ContactBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.EmailBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.PhoneBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.SmsBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.TextBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.UrlBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.WifiBarcode
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class BarcodeType(
    val datatype: Class<*>,
    val typeId: Int,
    val typeNameResId: Int,
    val resIcon: Int
) : Parcelable {
    object Email : BarcodeType(
        EmailBarcode::class.java, 1, R.string.email, R.drawable.ic_email
    )

    object Sms : BarcodeType(
        SmsBarcode::class.java, 2, R.string.sms, R.drawable.ic_sms
    )

    object Phone : BarcodeType(
        PhoneBarcode::class.java, 3, R.string.phone, R.drawable.ic_call
    )

    object Contact : BarcodeType(
        ContactBarcode::class.java, 4, R.string.contact, R.drawable.ic_contacts
    )

    object Text : BarcodeType(
        TextBarcode::class.java, 5, R.string.text, R.drawable.ic_text
    )

    object Url : BarcodeType(
        UrlBarcode::class.java, 6, R.string.website, R.drawable.ic_browser
    )

    object Wifi : BarcodeType(
        WifiBarcode::class.java, 7, R.string.wifi, R.drawable.ic_wifi
    )

    companion object {

        fun values() = listOf(
            Email, Sms, Phone, Contact, Text, Url, Wifi
        )

        fun getTypeFormId(typeId: Int): BarcodeType {
            return getTypeFormIdOrNull(typeId) ?: Text
        }

        fun getTypeFormIdOrNull(typeId: Int): BarcodeType? {
            return values().find { it.typeId == typeId }
        }

        val DIFF = object : DiffUtil.ItemCallback<BarcodeType>() {
            override fun areItemsTheSame(oldItem: BarcodeType, newItem: BarcodeType): Boolean {
                return oldItem.typeId == newItem.typeId
            }

            override fun areContentsTheSame(oldItem: BarcodeType, newItem: BarcodeType): Boolean {
                return oldItem == newItem
            }
        }
    }
}