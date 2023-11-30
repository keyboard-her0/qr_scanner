package com.keyboardhero.qr.core.router

import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import javax.inject.Inject

class RouterImpl @Inject constructor() : Router {

    private var navController: NavController? = null

    fun init(navController: NavController) {
        this.navController = navController
    }


    override fun navigate(screen: Screen) {
        navController?.navigate(resId = screen.getScreenId())
    }

    override fun navigate(screen: Screen, data: Bundle) {
        navController?.navigate(resId = screen.getScreenId(), args = data)
    }

    override fun navigate(screen: Screen, data: Bundle, navOptions: NavOptions) {
        navController?.navigate(
            resId = screen.getScreenId(),
            args = data,
            navOptions = navOptions
        )
    }

    override fun backToPreviousScreen(): Boolean {
        return navController?.popBackStack() ?: false
    }

    override fun backToPreviousScreen(destinationScreen: Screen): Boolean {
        return navController?.popBackStack(
            destinationId = destinationScreen.getScreenId(),
            inclusive = false
        ) ?: false
    }
}