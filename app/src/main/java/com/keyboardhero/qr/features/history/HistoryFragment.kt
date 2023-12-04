package com.keyboardhero.qr.features.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.databinding.FragmentHistoryBinding
import com.keyboardhero.qr.features.main.HomePageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHistoryBinding
        get() = FragmentHistoryBinding::inflate

    private lateinit var pageAdapter : HomePageAdapter
    private val tabs = mapOf(
        "Scan" to HistoryListFragment(), "Generate" to HistoryListFragment()
    )

    override fun initData(data: Bundle?) {

    }

    override fun initViews() {
        initHeaderAppBar()
        initViewPager()
    }

    private fun initViewPager() {
        pageAdapter = HomePageAdapter(this)
        pageAdapter.addScreen(tabs.values.toList())
        binding.viewPager.apply {
            adapter = pageAdapter
            offscreenPageLimit = tabs.size
        }

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            tab.setText(tabs.keys.toList()[position])
        }.attach()
    }

    override fun initHeaderAppBar() {
        headerAppBar.title = getString(R.string.bottom_navigation_history)
    }

    override fun initActions() {
    }

    override fun initObservers() {
    }
}