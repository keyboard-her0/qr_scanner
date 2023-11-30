package com.keyboardhero.qr.features.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseBottomSheetDialogFragment
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.databinding.FragmentSettingsBinding
import com.keyboardhero.qr.features.settings.theme.SelectThemeFragment
import com.keyboardhero.qr.shared.domain.dto.ThemeSetting
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding
        get() = FragmentSettingsBinding::inflate

    private val viewModel: SettingViewModel by viewModels()

    override fun initData(data: Bundle?) {
    }

    override fun initViews() {
    }

    override fun initHeaderAppBar() {
        headerAppBar.title = getString(R.string.bottom_navigation_settings)
        headerAppBar.titleCentered = true
    }

    override fun initActions() {
//        binding.btnChangeTheme.setOnClickListener {
//            val themes = listOf(
//                ThemeSetting.Theme(
//                    title = "Auto",
//                    resIconId = 0,
//                    type = ThemeSetting.ThemeType.AUTO
//                ),
//                ThemeSetting.Theme(
//                    title = "Day",
//                    resIconId = 0,
//                    type = ThemeSetting.ThemeType.DAY
//                ),
//                ThemeSetting.Theme(
//                    title = "Night",
//                    resIconId = 0,
//                    type = ThemeSetting.ThemeType.NIGHT
//                ),
//            )
//            val bottomSheet = SelectThemeFragment.newInstance(themes = themes) { theme ->
//                viewModel.switchThemeMode(theme.type.ordinal)
//            }
//            bottomSheet.show(childFragmentManager)
//        }
    }

    override fun initObservers() {
    }
}