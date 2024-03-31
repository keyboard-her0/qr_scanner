package com.keyboardhero.qr.features.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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

    private lateinit var pageAdapter: HomePageAdapter

    private val viewModel: HistoryViewModel by viewModels()

    override fun initData(data: Bundle?) {
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllHistory()
    }

    override fun initViews() {
        initHeaderAppBar()
        initViewPager()
    }

    private fun initViewPager() {
        val tabs = mapOf(
            getString(R.string.scanned) to HistoryListFragment.newInstance(true),
            getString(R.string.created) to HistoryListFragment.newInstance(false)
        )

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
        headerAppBar.title = getString(R.string.history)
        headerAppBar.navigationIconId = R.drawable.ic_back_24
        headerAppBar.titleCentered = true
    }

    override fun initActions() {
        headerAppBar.navigationOnClickListener = { onBackPressed() }
    }

    override fun initObservers() {
    }

    fun getHistoryViewModel(): HistoryViewModel = viewModel
}