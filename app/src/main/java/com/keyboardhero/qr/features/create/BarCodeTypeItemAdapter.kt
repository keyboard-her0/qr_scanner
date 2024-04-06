package com.keyboardhero.qr.features.create

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keyboardhero.qr.databinding.LayoutGenerateItemBinding
import com.keyboardhero.qr.shared.domain.dto.BarcodeType

class BarCodeTypeItemAdapter :
    ListAdapter<BarcodeType, BarCodeTypeItemAdapter.GenerateItemViewHolder>(BarcodeType.DIFF) {

    var onItemClick: ((BarcodeType) -> Unit)? = null

    inner class GenerateItemViewHolder(
        private val binding: LayoutGenerateItemBinding,
        onItemClick: ((BarcodeType) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(adapterPosition))
            }
        }

        fun bind(barcodeType: BarcodeType) {
            with(binding) {
                imgItem.setImageResource(barcodeType.resIcon)
                tvTitle.text = itemView.context.getString(barcodeType.typeNameResId)
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