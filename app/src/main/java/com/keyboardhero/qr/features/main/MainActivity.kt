package com.keyboardhero.qr.features.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import com.keyboardhero.qr.core.base.BaseActivity
import com.keyboardhero.qr.core.utils.CommonUtils
import com.keyboardhero.qr.databinding.ActivityMainBinding
import com.keyboardhero.qr.shared.data.AppPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    @Inject
    lateinit var appPreference: AppPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initActions()
    }

    private fun initActions() {
        // do nothing
    }

    @SuppressLint("ResourceType")
    private fun initViews() {
        initThemes()
//        if (isGestureNavigationEnabled(contentResolver = contentResolver)) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//            window.navigationBarColor = Color.TRANSPARENT
//        } else {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//        }
    }

    private fun initThemes() {
        val typeTheme = appPreference.theme
        CommonUtils.switchThemeMode(typeTheme)
    }
}
