package com.keyboardhero.qr.features.generate.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.databinding.FragmentGenerateResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenerateResultFragment : BaseFragment<FragmentGenerateResultBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentGenerateResultBinding
        get() = FragmentGenerateResultBinding::inflate

    override fun initData(data: Bundle?) {
    }

    override fun initViews() {
    }

    override fun initHeaderAppBar() {
        headerAppBar.title = getString(R.string.title_generate_result_screen)
        headerAppBar.titleCentered = true
        headerAppBar.navigationIconId = R.drawable.ic_back_24
    }

    override fun initActions() {
        headerAppBar.navigationOnClickListener = {
            onBackPressed()
        }
    }

    override fun initObservers() {
    }

}