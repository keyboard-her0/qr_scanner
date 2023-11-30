package com.keyboardhero.qr.shared.domain.dto

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.parcel.Parcelize

object ThemeSetting {

    @Parcelize
    data class Theme(
        val title: String,
        val resIconId: Int,
        val type: ThemeType = DEFAULT_THEME_MODE,
        val isSelected: Boolean = false
    ) : Parcelable {
        companion object {
            val DIFF = object : DiffUtil.ItemCallback<Theme>() {
                override fun areItemsTheSame(
                    oldItem: Theme,
                    newItem: Theme
                ): Boolean {
                    return oldItem.type == newItem.type
                }

                override fun areContentsTheSame(
                    oldItem: Theme,
                    newItem: Theme
                ): Boolean {
                    return newItem == oldItem
                }
            }
        }
    }

    enum class ThemeType {
        AUTO,
        DAY,
        NIGHT,
    }

    val DEFAULT_THEME_MODE = ThemeType.AUTO
}

