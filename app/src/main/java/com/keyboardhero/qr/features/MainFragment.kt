package com.keyboardhero.qr.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMainBinding
        get() = FragmentMainBinding::inflate

    override fun initData(data: Bundle?) {
        // initData
    }

    override fun initViews() {
    }

    override fun initActions() {
        binding.btnScanNow.setOnClickListener {
            val action = MainFragmentDirections.actionMainScreenToScannerFragment()
            findNavController().navigate(action)
        }
    }

    override fun initObservers() {
    }
}