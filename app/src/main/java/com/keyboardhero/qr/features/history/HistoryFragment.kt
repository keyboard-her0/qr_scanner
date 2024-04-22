package com.keyboardhero.qr.features.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

    private val historyScanFragment = HistoryListFragment.newInstance(true)
    private val historyCreateFragment = HistoryListFragment.newInstance(false)
    private lateinit var tabs : Map<String, Fragment>

    override fun initData(data: Bundle?) {
        viewModel.getAllHistory()
        tabs = mapOf(
            getString(R.string.scanned) to historyScanFragment,
            getString(R.string.created) to historyCreateFragment
        )
    }

    override fun initViews() {
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
        headerAppBar.title = getString(R.string.history)
        headerAppBar.navigationIconId = R.drawable.ic_back_24
        headerAppBar.titleCentered = true
    }

    override fun initActions() {
        headerAppBar.navigationOnClickListener = { onBackPressed() }
    }

    override fun initObservers() {
//        viewModel.observe(
//            owner = viewLifecycleOwner,
//            selector = { state -> state.loading },
//            observer = { loading ->
//                if (loading) showLoading() else hideLoading()
//            }
//        )
    }

    fun getHistoryViewModel(): HistoryViewModel = viewModel
}