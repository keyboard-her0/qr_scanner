package com.keyboardhero.qr.features.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keyboardhero.qr.R
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

    override fun onBindViewHolder(
        holder: HistoryViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach { payload ->
                if (payload == PAYLOAD_FAVORITE) {
                    holder.bindFavorite(getItem(position))
                }
            }
        }
    }

    class HistoryViewHolder(
        private val binding: LayoutHistoryItemBinding,
        listener: HistoryListener? = null
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var historyDTO: HistoryDTO
        init {
            with(binding) {
                itemView.setOnClickListener {
                    listener?.onItemClick(historyDTO)
                }

                imgFavorite.setOnClickListener {
                    listener?.onFavoriteClick(historyDTO)
                }
            }
        }

        fun bind(history: HistoryDTO) {
            historyDTO = history
            with(binding) {
                tvTitle.text = history.id.toString()
                tvCreateAt.text = history.createAt.toString()
            }
            bindFavorite(history)
        }

        fun bindFavorite(history: HistoryDTO) {
            historyDTO = history
            binding.imgFavorite.setImageResource(if (history.favorite) R.drawable.ic_favorite_24 else R.drawable.ic_favorite_border_24)
        }
    }

    companion object {
        private const val PAYLOAD_FAVORITE = 0

        private val diff = object : DiffUtil.ItemCallback<HistoryDTO>() {
            override fun areItemsTheSame(oldItem: HistoryDTO, newItem: HistoryDTO): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HistoryDTO, newItem: HistoryDTO): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(oldItem: HistoryDTO, newItem: HistoryDTO): Any? {
                if (oldItem.favorite != newItem.favorite) return PAYLOAD_FAVORITE
                return super.getChangePayload(oldItem, newItem)
            }
        }
    }


    interface HistoryListener {
        fun onItemClick(history: HistoryDTO)
        fun onFavoriteClick(history: HistoryDTO)
    }
}