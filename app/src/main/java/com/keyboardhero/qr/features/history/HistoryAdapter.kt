package com.keyboardhero.qr.features.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keyboardhero.qr.databinding.LayoutHistoryItemBinding
import com.keyboardhero.qr.shared.domain.dto.HistoryDTO

class HistoryAdapter : ListAdapter<HistoryDTO, HistoryAdapter.HistoryViewHolder>(diff) {

    var listener: HistoryListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = LayoutHistoryItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HistoryViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HistoryViewHolder(
        private val binding: LayoutHistoryItemBinding,
        listener: HistoryListener? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var historyDTO: HistoryDTO

        init {
            itemView.setOnClickListener {
                listener?.onItemClick(historyDTO)
            }
        }

        fun bind(history: HistoryDTO) {
            historyDTO = history
            with(binding) {
                tvTitle.text = itemView.context.getString(history.barcodeType.typeNameResId)
                tvDescription.text = history.barcodeData.getInputData()
                tvCreateAt.text = history.createAt.toString()
                imgIcon.setImageResource(history.barcodeType.resIcon)
            }
        }
    }

    companion object {
        private val diff = object : DiffUtil.ItemCallback<HistoryDTO>() {
            override fun areItemsTheSame(oldItem: HistoryDTO, newItem: HistoryDTO): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HistoryDTO, newItem: HistoryDTO): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface HistoryListener {
        fun onItemClick(history: HistoryDTO)
    }
}