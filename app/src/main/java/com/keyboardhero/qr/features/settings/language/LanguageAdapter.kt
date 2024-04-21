package com.keyboardhero.qr.features.settings.language

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keyboardhero.qr.databinding.ItemSelectLayoutBinding
import com.keyboardhero.qr.shared.domain.dto.Language

class LanguageAdapter : ListAdapter<Language, LanguageAdapter.LanguageViewHolder>(Language.DIFF) {

    var onClick: ((Language) -> Unit)? = null

    inner class LanguageViewHolder(
        private val binding: ItemSelectLayoutBinding,
        onThemeClick: ((Language) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onThemeClick?.invoke(getItem(adapterPosition))
            }
        }

        fun bind(language: Language) {
            with(binding) {
                tvTitle.text = itemView.context.getString(language.nameResId)
                imgCheck.isVisible = language.isSelected
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding =
            ItemSelectLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LanguageViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}