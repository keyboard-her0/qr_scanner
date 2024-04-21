package com.keyboardhero.qr.features.settings.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.base.BaseBottomSheetDialogFragment
import com.keyboardhero.qr.core.base.BaseFragment
import com.keyboardhero.qr.core.base.dismissBottomSheet
import com.keyboardhero.qr.databinding.FragmentSelectThemeBinding
import com.keyboardhero.qr.shared.domain.dto.Language

class SelectLanguageFragment : BaseFragment<FragmentSelectThemeBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSelectThemeBinding
        get() = FragmentSelectThemeBinding::inflate

    private var onSelectLanguage: ((Language) -> Unit)? = null

    private val languages = mutableListOf<Language>()
    private val languageAdapter: LanguageAdapter by lazy { LanguageAdapter() }

    override fun initData(data: Bundle?) {
        if (data != null) {
            languages.clear()
            languages.addAll(
                data.getParcelableArrayList(GET_LANGUAGES_KEY) ?: emptyList()
            )
            languageAdapter.submitList(languages)
        }
    }

    override fun initViews() {
        with(binding) {
            tvHeader.text = getString(R.string.select_language)
            rvThemes.apply {
                adapter = languageAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    override fun initHeaderAppBar() {
        headerAppBar.isVisible = false
    }

    override fun initActions() {
        languageAdapter.onClick = {
            onSelectLanguage?.invoke(it)
            dismissBottomSheet()
        }
    }

    override fun initObservers() {
        //Do nothing
    }

    companion object {
        fun newInstance(
            languages: List<Language>,
            onSelectLanguage: ((Language) -> Unit)
        ): BaseBottomSheetDialogFragment {
            return BaseBottomSheetDialogFragment.newInstance(
                SelectLanguageFragment().apply {
                    arguments = bundleOf(
                        GET_LANGUAGES_KEY to arrayListOf<Language>().apply { addAll(languages) }
                    )
                    this.onSelectLanguage = onSelectLanguage
                }
            )
        }

        private const val GET_LANGUAGES_KEY = "GET_LANGUAGES_KEY"
    }
}