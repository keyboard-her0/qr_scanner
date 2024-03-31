package com.keyboardhero.qr.features.create

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.keyboardhero.qr.shared.domain.dto.BarcodeType

data class CreateTypeItem(
    val titleResId: Int,
    val resIconId: Int,
    val type: BarcodeType
) {
    companion object {
        val DIFF = object : ItemCallback<CreateTypeItem>() {
            override fun areItemsTheSame(oldItem: CreateTypeItem, newItem: CreateTypeItem): Boolean {
                return oldItem.titleResId == newItem.titleResId
            }

            override fun areContentsTheSame(oldItem: CreateTypeItem, newItem: CreateTypeItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
