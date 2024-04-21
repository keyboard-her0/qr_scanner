package com.keyboardhero.qr.features.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.WindowCompat
import com.keyboardhero.qr.core.base.BaseActivity
import com.keyboardhero.qr.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    @SuppressLint("ResourceType")
    private fun initViews() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}
