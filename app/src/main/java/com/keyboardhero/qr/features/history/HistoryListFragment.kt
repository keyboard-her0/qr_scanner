package com.keyboardhero.qr.features.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.databinding.FragmentHistoryListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryListFragment : BaseFragment<FragmentHistoryListBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHistoryListBinding
        get() = FragmentHistoryListBinding::inflate

    private var viewModel: HistoryViewModel? = null
    private var isFavorite = false
    private val historyAdapter by lazy { HistoryAdapter() }

    override fun initData(data: Bundle?) {
        (parentFragment as? HistoryFragment)?.let { historyFragment ->
            viewModel = historyFragment.getHistoryViewModel()
        }
    }

    override fun initViews() {
        with(binding) {
            rvHistory.adapter = historyAdapter
            rvHistory.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun initHeaderAppBar() {
        headerAppBar.isVisible = false
    }

    override fun initActions() {
    }

    override fun initObservers() {
        viewModel?.observe(
            owner = viewLifecycleOwner,
            selector = { state -> state.listHistory },
            observer = { listHistory ->
                if (isFavorite) {
                    historyAdapter.submitList(listHistory.filter { isFavorite })
                } else {
                    historyAdapter.submitList(listHistory)
                }
            }
        )
    }
}