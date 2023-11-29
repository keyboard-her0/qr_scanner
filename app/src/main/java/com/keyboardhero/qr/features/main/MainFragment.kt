package com.keyboardhero.qr.features.main

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.databinding.FragmentMainBinding
import com.keyboardhero.qr.features.generate.GenerateFragment
import com.keyboardhero.qr.features.history.HistoryFragment
import com.keyboardhero.qr.features.scan.ScanFragment
import com.keyboardhero.qr.features.settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.Q)
class MainFragment : BaseFragment<FragmentMainBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate


    private val homePageAdapter: HomePageAdapter by lazy { HomePageAdapter(this) }
    private val screens = listOf(
        ScanFragment(),
        GenerateFragment(),
        HistoryFragment(),
        SettingsFragment(),
    )

    override fun initData(data: Bundle?) {

    }

    override fun initViews() {
        initViewPager()
    }

    private fun initViewPager() {
        homePageAdapter.addScreen(screens)
        binding.viewPager.apply {
            adapter = homePageAdapter
            isUserInputEnabled = false
            offscreenPageLimit = homePageAdapter.itemCount
        }
    }

    override fun initActions() {
        handleBottomNavigationAction()
    }

    private fun handleBottomNavigationAction() {
        with(binding) {
            bottomNavigation.setOnItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.scanFragment -> {
                        viewPager.currentItem = 0
                        true
                    }

                    R.id.generateFragment -> {
                        viewPager.currentItem = 1
                        true
                    }

                    R.id.historyFragment -> {
                        viewPager.currentItem = 2
                        true
                    }

                    R.id.settingsFragment -> {
                        viewPager.currentItem = 3
                        true
                    }

                    else -> {
                        //Do nothing
                        false
                    }
                }
            }
        }
    }

    fun getCurrentPage(): Fragment? {
        val currentItem = binding.viewPager.currentItem
        return screens.getOrNull(currentItem)
    }

    override fun initObservers() {
    }
}
