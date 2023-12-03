package com.keyboardhero.qr.features.settings.theme

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keyboardhero.qr.databinding.ItemSelectLayoutBinding
import com.keyboardhero.qr.shared.domain.dto.ThemeSetting

class ThemeAdapter :
    ListAdapter<ThemeSetting.Theme, ThemeAdapter.ThemeViewHolder>(ThemeSetting.Theme.DIFF) {

    var onThemeClick: ((ThemeSetting.Theme) -> Unit)? = null

    inner class ThemeViewHolder(
        private val binding: ItemSelectLayoutBinding,
        onThemeClick: ((ThemeSetting.Theme) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onThemeClick?.invoke(getItem(adapterPosition))
            }
        }

        fun bind(theme: ThemeSetting.Theme) {
            with(binding) {
                tvTitle.text = theme.title
                imgCheck.isVisible = theme.isSelected
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val binding =
            ItemSelectLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThemeViewHolder(binding, onThemeClick)
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}