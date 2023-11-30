package com.keyboardhero.qr.features.generate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keyboardhero.qr.databinding.LayoutGenerateItemBinding

class GenerateItemAdapter :
    ListAdapter<GenerateItem, GenerateItemAdapter.GenerateItemViewHolder>(GenerateItem.DIFF) {

    var onItemClick: ((GenerateItem) -> Unit)? = null

    inner class GenerateItemViewHolder(
        private val binding: LayoutGenerateItemBinding,
        onItemClick: ((GenerateItem) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(adapterPosition))
            }
        }

        fun bind(generateItem: GenerateItem) {
            with(binding) {
                imgItem.setImageResource(generateItem.resIconId)
                tvTitle.text = generateItem.title
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