package com.keyboardhero.qr.shared.domain.dto

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class Language(
    val code: String,
    val nameResId: Int,
    val isSelected: Boolean
) : Parcelable {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Language>() {
            override fun areItemsTheSame(oldItem: Language, newItem: Language): Boolean =
                oldItem.code == newItem.code

            override fun areContentsTheSame(oldItem: Language, newItem: Language): Boolean =
                oldItem == newItem
        }
    }
}