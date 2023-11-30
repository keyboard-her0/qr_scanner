package com.keyboardhero.qr.features.generate

import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel @Inject constructor() :
    BaseViewModel<GenerateViewState, GenerateViewEvents>() {
    override fun initState(): GenerateViewState = GenerateViewState(false, generateItems)

    private val generateItems = listOf(
        GenerateItem(
            title = "Web",
            resIconId = R.drawable.ic_web_24,
            type = GenerateType.WEB
        ),
        GenerateItem(
            title = "Wifi",
            resIconId = R.drawable.ic_wifi_24,
            type = GenerateType.WIFI
        ),
        GenerateItem(
            title = "Contact",
            resIconId = R.drawable.ic_contact_24,
            type = GenerateType.CONTACT
        ),
        GenerateItem(
            title = "Text",
            resIconId = R.drawable.ic_text_24,
            type = GenerateType.TEXT
        ),
    )
}