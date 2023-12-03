package com.keyboardhero.qr.features.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.databinding.FragmentSettingsBinding
import com.keyboardhero.qr.features.settings.theme.SelectThemeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding
        get() = FragmentSettingsBinding::inflate

    private val viewModel: SettingViewModel by viewModels()

    override fun initData(data: Bundle?) {
        viewModel.initThemes()
    }

    override fun initViews() {
        initChangeThemeItem()
    }

    private fun initChangeThemeItem() {
        with(binding){
            itemChangeTheme.apply {
                tvTitle.text = "Change Themes"
            }
        }
    }

    override fun initHeaderAppBar() {
        headerAppBar.title = getString(R.string.bottom_navigation_settings)
    }

    override fun initActions() {
        binding.itemChangeTheme.root.setOnClickListener {
            val bottomSheet = SelectThemeFragment.newInstance(themes = viewModel.currentState.themes) { theme ->
                viewModel.changeTheme(theme)
            }
            bottomSheet.show(childFragmentManager)
        }
    }

    override fun initObservers() {
    }
}