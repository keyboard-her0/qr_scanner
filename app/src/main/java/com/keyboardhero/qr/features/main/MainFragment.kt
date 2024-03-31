package com.keyboardhero.qr.features.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.databinding.FragmentMainBinding
import com.keyboardhero.qr.features.create.CreateScreen
import com.keyboardhero.qr.features.history.HistoryScreen
import com.keyboardhero.qr.features.scan.ScanScreen
import com.keyboardhero.qr.features.settings.SettingsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    override fun initData(data: Bundle?) {
        //Do nothing
    }

    override fun initViews() {
        //Do nothing
    }

    override fun initHeaderAppBar() {
        headerAppBar.isVisible = false
    }

    override fun initActions() {
        with(binding) {
            itemScan.setOnClickListener {
                router.navigate(ScanScreen)
            }
            itemCreate.setOnClickListener {
                router.navigate(CreateScreen)
            }
            itemHistory.setOnClickListener {
                router.navigate(HistoryScreen)
            }
            itemSettings.setOnClickListener {
                router.navigate(SettingsScreen)
            }
        }
    }

    override fun initObservers() {
        //Do nothing
    }
}
