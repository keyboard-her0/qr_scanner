package com.keyboardhero.qr.shared.domain.dto

import com.keyboardhero.qr.shared.domain.dto.barcodedata.EmailBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.SmsBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.TextBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.UrlBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.VcardBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.WifiBarcode

enum class BarcodeType(val datatype: Class<*>, val typeId: Int) {
    EMAIl(EmailBarcode::class.java, 1),
    SMS(SmsBarcode::class.java, 2),
    TEXT(TextBarcode::class.java, 3),
    URL(UrlBarcode::class.java, 4),
    WIFI(WifiBarcode::class.java, 5),
    VCARD(VcardBarcode::class.java, 6);

    companion object {
        fun getTypeFormId(typeId: Int): BarcodeType {
            return getTypeFormIdOrNull(typeId) ?: TEXT
        }

        fun getTypeFormIdOrNull(typeId: Int): BarcodeType? {
            return values().find { it.typeId == typeId }
        }
    }
}