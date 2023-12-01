package com.keyboardhero.qr.features.main

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import com.keyboardhero.qr.core.base.BaseActivity
import com.keyboardhero.qr.core.utils.CommonUtils.isGestureNavigationEnabled
import com.keyboardhero.qr.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

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
//        if (isGestureNavigationEnabled(contentResolver = contentResolver)) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//            window.navigationBarColor = Color.TRANSPARENT
//        } else {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//        }
    }
}
