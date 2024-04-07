package com.keyboardhero.qr.shared.domain.dto.barcodedata

import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactBarcode(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val company: String,
    val jobTitle: String,
    val street: String,
    val city: String,
    val zip: String,
    val state: String,
    val country: String,
    val website: String,
) : BarcodeData {
    override fun getInputData(): String {
        return "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "N:$lastName;$firstName\n" +
                "FN:$firstName $lastName\n" +
                "ORG:$company\n" +
                "TITLE:$jobTitle\n" +
                "ADR:;;$street;$city;$state;$zip;$country\n" +
                "TEL;WORK;VOICE:\n" +
                "TEL;CELL:$phoneNumber\n" +
                "TEL;FAX:\n" +
                "EMAIL;WORK;INTERNET:$email\n" +
                "URL:$website\n" +
                "END:VCARD"
    }
}