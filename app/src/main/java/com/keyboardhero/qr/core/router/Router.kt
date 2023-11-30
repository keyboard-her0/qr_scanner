package com.keyboardhero.qr.core.router

import android.os.Bundle
import androidx.navigation.NavOptions

interface Router {
    fun navigate(screen: Screen)

    fun navigate(screen: Screen, data: Bundle)

    fun navigate(screen: Screen, data: Bundle, navOptions: NavOptions)

    fun backToPreviousScreen(): Boolean

    fun backToPreviousScreen(destinationScreen: Screen): Boolean
}