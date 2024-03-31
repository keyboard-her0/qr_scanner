package com.keyboardhero.qr.features.create

import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseViewModel
import com.keyboardhero.qr.shared.domain.dto.BarcodeType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor() :
    BaseViewModel<CreateViewState, CreateViewEvents>() {
    override fun initState(): CreateViewState = CreateViewState(false, generateItems)

    private val generateItems = listOf(
        CreateTypeItem(
            titleResId = R.string.text,
            resIconId = R.drawable.ic_text_24,
            type = BarcodeType.TEXT
        ),
        CreateTypeItem(
            titleResId = R.string.phone,
            resIconId = R.drawable.ic_text_24,
            type = BarcodeType.PHONE
        ),
        CreateTypeItem(
            titleResId = R.string.sms,
            resIconId = R.drawable.ic_text_24,
            type = BarcodeType.SMS
        ),
        CreateTypeItem(
            titleResId = R.string.contact,
            resIconId = R.drawable.ic_contact_24,
            type = BarcodeType.VCARD
        ),
        CreateTypeItem(
            titleResId = R.string.wifi,
            resIconId = R.drawable.ic_wifi_24,
            type = BarcodeType.WIFI
        ),
        CreateTypeItem(
            titleResId = R.string.website,
            resIconId = R.drawable.ic_web_24,
            type = BarcodeType.URL
        ),
    )
}