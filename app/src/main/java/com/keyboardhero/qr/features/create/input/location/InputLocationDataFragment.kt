package com.keyboardhero.qr.features.create.input.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.keyboardhero.qr.R
import com.keyboardhero.qr.databinding.FragmentInputLocationDataBinding
import com.keyboardhero.qr.features.create.input.BaseInputFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputLocationDataFragment : BaseInputFragment<FragmentInputLocationDataBinding>() {
    override val title: String
        get() = getString(R.string.location)
    override val bottomLayoutContainer: View
        get() = binding.layoutBottom
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInputLocationDataBinding
        get() = FragmentInputLocationDataBinding::inflate

    override fun initDataInput(data: Bundle?) {

    }

    override fun initViewsInput() {

    }

    override fun initObserversInput() {

    }

    private fun checkValidInput() {

    }

    override fun navigateToResultScreen() {

    }
}