package com.keyboardhero.qr.core.router

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.keyboardhero.qr.R
import javax.inject.Inject

class RouterImpl @Inject constructor() : Router {

    private var navController: NavController? = null

    fun init(navController: NavController) {
        this.navController = navController
    }


    override fun navigate(screen: Screen) {
        //navController?.navigate(resId = screen.getScreenId())
        navigateWithAnimation(screen = screen)
    }

    override fun navigate(screen: Screen, data: Bundle) {
        //navController?.navigate(resId = screen.getScreenId(), args = data)
        navigateWithAnimation(screen = screen, data = data)
    }

    override fun navigate(screen: Screen, data: Bundle, navOptions: NavOptions) {
//        navController?.navigate(
//            resId = screen.getScreenId(),
//            args = data,
//            navOptions = navOptions
//        )
        navigateWithAnimation(screen = screen, data = data, options = navOptions)
    }

    override fun backToPreviousScreen(): Boolean {
        return navController?.navigateUp() ?: false
    }

    override fun backToPreviousScreen(destinationScreen: Screen): Boolean {
        return navController?.popBackStack(
            destinationId = destinationScreen.getScreenId(),
            inclusive = false
        ) ?: false
    }

    private fun navigateWithAnimation(
        screen: Screen,
        data: Bundle? = null,
        options: NavOptions? = null
    ) {
        val navOptionsBuilder = NavOptions.Builder()
        if (options != null) {
            navOptionsBuilder.apply {
                setRestoreState(options.shouldRestoreState())
                setLaunchSingleTop(options.shouldLaunchSingleTop())
                setPopUpTo(options.popUpToId, options.isPopUpToInclusive())
                setEnterAnim(if (options.enterAnim != -1) options.enterAnim else R.anim.slide_in_rtl_screen)
                setExitAnim(if (options.exitAnim != -1) options.exitAnim else R.anim.slide_out_rtl_screen)
                setPopEnterAnim(if (options.popEnterAnim != -1) options.popEnterAnim else R.anim.slide_in_ltr_screen)
                setPopExitAnim(if (options.popExitAnim != -1) options.popExitAnim else R.anim.slide_out_ltr_screen)
            }
        } else {
            navOptionsBuilder.apply {
                setEnterAnim(R.anim.slide_in_rtl_screen)
                setExitAnim(R.anim.slide_out_rtl_screen)
                setPopEnterAnim(R.anim.slide_in_ltr_screen)
                setPopExitAnim(R.anim.slide_out_ltr_screen)
            }
        }

        navController?.navigate(
            resId = screen.getScreenId(),
            args = data,
            navOptions = navOptionsBuilder.build()
        )
    }
}