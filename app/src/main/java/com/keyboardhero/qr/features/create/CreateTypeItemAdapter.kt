package com.keyboardhero.qr.features.create

import android.media.browse.MediaBrowser.ItemCallback
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keyboardhero.qr.databinding.LayoutGenerateItemBinding
import com.keyboardhero.qr.shared.domain.dto.BarcodeType

class CreateTypeItemAdapter :
    ListAdapter<CreateTypeItem, CreateTypeItemAdapter.GenerateItemViewHolder>(CreateTypeItem.DIFF) {

    var onItemClick: ((CreateTypeItem) -> Unit)? = null

    inner class GenerateItemViewHolder(
        private val binding: LayoutGenerateItemBinding,
        onItemClick: ((CreateTypeItem) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(adapterPosition))
            }
        }

        fun bind(generateItem: CreateTypeItem) {
            with(binding) {
                imgItem.setImageResource(generateItem.resIconId)
                tvTitle.text = itemView.context.getString(generateItem.titleResId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenerateItemViewHolder {
        val binding = LayoutGenerateItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GenerateItemViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: GenerateItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}