package com.keyboardhero.qr.features.generate

import androidx.recyclerview.widget.DiffUtil.ItemCallback

data class GenerateItem(
    val title: String,
    val resIconId: Int,
    val type: GenerateType
) {
    companion object {
        val DIFF = object : ItemCallback<GenerateItem>() {
            override fun areItemsTheSame(oldItem: GenerateItem, newItem: GenerateItem): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: GenerateItem, newItem: GenerateItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
