package com.keyboardhero.qr.shared.domain.dto

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.zxing.BarcodeFormat
import com.keyboardhero.qr.R
import com.keyboardhero.qr.shared.domain.dto.barcodedata.AztecBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.ContactBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.DataMatrixBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.EmailBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.LocationBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.Pdf417Barcode
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
    val resIcon: Int,
    val barcodeFormat: BarcodeFormat
) : Parcelable {
    object Email : BarcodeType(
        EmailBarcode::class.java,
        1,
        R.string.email,
        R.drawable.ic_email,
        barcodeFormat = BarcodeFormat.QR_CODE
    )

    object Sms : BarcodeType(
        SmsBarcode::class.java, 2, R.string.sms, R.drawable.ic_sms,
        barcodeFormat = BarcodeFormat.QR_CODE
    )

    object Phone : BarcodeType(
        PhoneBarcode::class.java, 3, R.string.phone, R.drawable.ic_call,
        barcodeFormat = BarcodeFormat.QR_CODE
    )

    object Contact : BarcodeType(
        ContactBarcode::class.java, 4, R.string.contact, R.drawable.ic_contacts,
        barcodeFormat = BarcodeFormat.QR_CODE
    )

    object Text : BarcodeType(
        TextBarcode::class.java, 5, R.string.text, R.drawable.ic_text,
        barcodeFormat = BarcodeFormat.QR_CODE
    )

    object Url : BarcodeType(
        UrlBarcode::class.java, 6, R.string.website, R.drawable.ic_browser,
        barcodeFormat = BarcodeFormat.QR_CODE
    )

    object Wifi : BarcodeType(
        WifiBarcode::class.java, 7, R.string.wifi, R.drawable.ic_wifi,
        barcodeFormat = BarcodeFormat.QR_CODE
    )

    object Location : BarcodeType(
        LocationBarcode::class.java,
        typeId = 8,
        R.string.location,
        R.drawable.ic_location,
        barcodeFormat = BarcodeFormat.QR_CODE
    )

    object Aztec : BarcodeType(
        AztecBarcode::class.java,
        typeId = 9,
        R.string.aztec,
        R.drawable.ic_aztec_code,
        barcodeFormat = BarcodeFormat.AZTEC
    )

    object DataMatrix : BarcodeType(
        DataMatrixBarcode::class.java,
        typeId = 10,
        R.string.data_matrix,
        R.drawable.ic_data_matrix_code,
        barcodeFormat = BarcodeFormat.DATA_MATRIX
    )

    object Pdf417 : BarcodeType(
        Pdf417Barcode::class.java,
        typeId = 11,
        R.string.pdf_417,
        R.drawable.ic_pdf_417_code,
        barcodeFormat = BarcodeFormat.PDF_417
    )

    companion object {

        fun values() = listOf(
            Email, Sms, Phone, Text, Url, Wifi, Location, Aztec, DataMatrix, Pdf417
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