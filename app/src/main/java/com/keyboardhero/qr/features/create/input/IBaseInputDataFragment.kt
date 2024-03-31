package com.keyboardhero.qr.features.create.input

import android.os.Bundle

interface IBaseInputDataFragment {
    fun initDataInput(data: Bundle?)

    fun initViewsInput()

    fun initActionsInput()

    fun initObserversInput()

    fun navigateToResultScreen()
}