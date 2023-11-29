package com.keyboardhero.qr.features.main

import android.os.Bundle
import android.view.LayoutInflater
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
        initActions()
    }

    private fun initActions() {
        // do nothing
    }

    private fun initViews() {
    }
}