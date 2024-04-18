package com.keyboardhero.qr.features.settings

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.keyboardhero.qr.Constant.PRIVACY_URL
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.core.utils.CommonUtils
import com.keyboardhero.qr.core.utils.views.setMargin
import com.keyboardhero.qr.databinding.FragmentSettingsBinding
import com.keyboardhero.qr.features.settings.theme.SelectThemeFragment
import com.keyboardhero.qr.shared.data.AppPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding
        get() = FragmentSettingsBinding::inflate

    private val viewModel: SettingViewModel by viewModels()

    @Inject
    lateinit var appPreference: AppPreference

    override fun initData(data: Bundle?) {
        viewModel.initData()
    }

    override fun initViews() {
        initChangeThemeItem()
        initVibrateItem()
        initItemPrivacyPolicy()
        initItemAppVersion()


    }

    private fun initItemAppVersion() {
        with(binding) {
            tvVersion.apply {
                text = getString(R.string.app_version, CommonUtils.getAppVersion())
                setMargin(
                    bottom = CommonUtils.getNavigationBarHeight(requireContext())
                            + requireContext().resources.getDimensionPixelOffset(R.dimen.size_16dp)
                )
            }
        }
    }

    private fun initItemPrivacyPolicy() {
        with(binding) {
            itemPrivacyPolicy.tvTitle.text = getString(R.string.privacy_policy)
            itemPrivacyPolicy.startIcon.setImageResource(R.drawable.ic_privacy_policy)
        }
    }

    private fun initVibrateItem() {
        with(binding) {
            itemVibrate.startIcon.setImageResource(R.drawable.ic_vibration)
            itemVibrate.tvTitle.text = getString(R.string.vibrate)
            itemVibrate.tvDescription.text = getString(R.string.vibrate_message)
            itemVibrate.switchWidget.isChecked = appPreference.vibration
        }
    }

    private fun initChangeThemeItem() {
        with(binding) {
            itemChangeTheme.apply {
                tvTitle.text = getString(R.string.change_themes)
            }
        }
    }

    override fun initHeaderAppBar() {
        headerAppBar.title = getString(R.string.settings)
        headerAppBar.navigationIconId = R.drawable.ic_back_24
        headerAppBar.titleCentered = true
    }

    override fun initActions() {
        headerAppBar.navigationOnClickListener = {
            onBackPressed()
        }
        binding.itemChangeTheme.root.setOnClickListener {
            val bottomSheet =
                SelectThemeFragment.newInstance(themes = viewModel.currentState.themes) { theme ->
                    viewModel.changeTheme(theme)
                }
            bottomSheet.show(childFragmentManager)
        }

        binding.itemVibrate.switchWidget.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setVibration(isChecked)
        }

        binding.itemPrivacyPolicy.root.setOnClickListener {
            CommonUtils.openUriInApp(requireContext(), Uri.parse(PRIVACY_URL))
        }
    }

    override fun initObservers() {
    }
}