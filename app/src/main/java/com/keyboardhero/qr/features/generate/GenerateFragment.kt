package com.keyboardhero.qr.features.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.databinding.FragmentGenerateBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GenerateFragment : BaseFragment<FragmentGenerateBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGenerateBinding
        get() = FragmentGenerateBinding::inflate

    override fun initData(data: Bundle?) {
    }

    override fun initViews() {
    }

    override fun initActions() {
    }

    override fun initObservers() {
    }
}