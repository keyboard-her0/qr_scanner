package com.keyboardhero.qr.features.settings.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.core.base.dismissBottomSheet
import com.keyboardhero.qr.databinding.FragmentSelectThemeBinding
import com.keyboardhero.qr.shared.domain.dto.ThemeSetting

class SelectThemeFragment : BaseFragment<FragmentSelectThemeBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSelectThemeBinding
        get() = FragmentSelectThemeBinding::inflate

    private var onSelectTheme: ((ThemeSetting.Theme) -> Unit)? = null

    private val themes = mutableListOf<ThemeSetting.Theme>()
    private val themeAdapter: ThemeAdapter by lazy { ThemeAdapter() }

    override fun initData(data: Bundle?) {
        if (data != null) {
            themes.clear()
            themes.addAll(
                data.getParcelableArrayList(GET_THEMES_KEY) ?: emptyList()
            )
            themeAdapter.submitList(themes)
        }
    }

    override fun initViews() {
        with(binding) {
            rvThemes.apply {
                adapter = themeAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    override fun initHeaderAppBar() {
        headerAppBar.isVisible = false
    }

    override fun initActions() {
        themeAdapter.onThemeClick = {
            onSelectTheme?.invoke(it)
            dismissBottomSheet()
        }
    }

    override fun initObservers() {
        //Do nothing
    }

    companion object {
        fun newInstance(
            themes: List<ThemeSetting.Theme>,
            onSelectTheme: ((ThemeSetting.Theme) -> Unit)
        ): SelectThemeFragment {
            val fragment = SelectThemeFragment()
            fragment.arguments = Bundle().apply {
                putParcelableArrayList(
                    GET_THEMES_KEY,
                    arrayListOf<ThemeSetting.Theme>().apply { addAll(themes) })
            }
            fragment.onSelectTheme = onSelectTheme
            return fragment
        }

        private const val GET_THEMES_KEY = "GET_THEMES_KEY"
    }
}