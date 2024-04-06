package com.keyboardhero.qr.shared.domain.dto

import com.keyboardhero.qr.R
import com.keyboardhero.qr.shared.domain.dto.barcodedata.ContactBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.EmailBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.PhoneBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.SmsBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.TextBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.UrlBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.WifiBarcode

enum class BarcodeType(
    val datatype: Class<*>,
    val typeId: Int,
    val typeNameResId: Int,
    val resIcon: Int
) {
    EMAIl(EmailBarcode::class.java, 1, R.string.email, R.drawable.ic_text_24),
    SMS(SmsBarcode::class.java, 2, R.string.sms, R.drawable.ic_text_24),
    PHONE(PhoneBarcode::class.java, 3, R.string.phone, R.drawable.ic_text_24),
    CONTACT(ContactBarcode::class.java, 4, R.string.contact, R.drawable.ic_text_24),
    TEXT(TextBarcode::class.java, 5, R.string.text, R.drawable.ic_text_24),
    URL(UrlBarcode::class.java, 6, R.string.website, R.drawable.ic_text_24),
    WIFI(WifiBarcode::class.java, 7, R.string.wifi, R.drawable.ic_text_24);

    companion object {
        fun getTypeFormId(typeId: Int): BarcodeType {
            return getTypeFormIdOrNull(typeId) ?: TEXT
        }

        fun getTypeFormIdOrNull(typeId: Int): BarcodeType? {
            return values().find { it.typeId == typeId }
        }
    }
}