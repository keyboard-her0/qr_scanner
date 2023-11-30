package com.keyboardhero.qr.features.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.databinding.FragmentGenerateBinding
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
        headerAppBar.titleCentered = true
    }

    override fun initActions() {
        generateItemAdapter.onItemClick = {
           mNavController?.navigate(R.id.generateResultFragment)
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