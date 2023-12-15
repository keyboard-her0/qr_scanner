package com.keyboardhero.qr.features.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.databinding.FragmentGenerateBinding
import com.keyboardhero.qr.features.generate.result.GenerateResultFragmentArgs
import com.keyboardhero.qr.features.generate.result.GenerateResultScreen
import com.keyboardhero.qr.shared.domain.dto.input.TextBarcode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenerateFragment : BaseFragment<FragmentGenerateBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGenerateBinding
        get() = FragmentGenerateBinding::inflate

    private val generateItemAdapter: GenerateItemAdapter by lazy { GenerateItemAdapter() }
    private val viewModel: GenerateViewModel by viewModels()

    override fun initData(data: Bundle?) {
    }

    override fun initViews() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(binding) {
            rvItem.adapter = generateItemAdapter
            rvItem.layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    override fun initHeaderAppBar() {
        headerAppBar.title = getString(R.string.bottom_navigation_generate)
    }

    override fun initActions() {
        generateItemAdapter.onItemClick = {
            router.navigate(
                GenerateResultScreen,
                GenerateResultFragmentArgs(TextBarcode("123456")).toBundle()
            )
        }
    }

    override fun initObservers() {
        viewModel.observe(
            owner = viewLifecycleOwner,
            selector = { state -> state.generateItem },
            observer = { generateItems -> generateItemAdapter.submitList(generateItems) }
        )
    }
}