package com.keyboardhero.qr.features.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keyboardhero.qr.R
import com.keyboardhero.qr.core.utils.CommonUtils
import com.keyboardhero.qr.core.utils.views.setMargin
import com.keyboardhero.qr.databinding.LayoutHistoryItemBinding
import com.keyboardhero.qr.shared.domain.dto.HistoryDTO
import com.keyboardhero.qr.shared.domain.dto.barcodedata.BarcodeData
import com.keyboardhero.qr.shared.domain.dto.barcodedata.ContactBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.EmailBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.PhoneBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.SmsBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.TextBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.UrlBarcode
import com.keyboardhero.qr.shared.domain.dto.barcodedata.WifiBarcode

class HistoryAdapter : ListAdapter<HistoryDTO, HistoryAdapter.HistoryViewHolder>(diff) {

    var listener: HistoryListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = LayoutHistoryItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HistoryViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position), position == itemCount - 1)
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

        fun bind(history: HistoryDTO, showSpace: Boolean) {
            val context = itemView.context
            historyDTO = history
            if (showSpace) {
                val space = context.resources.getDimensionPixelOffset(
                    R.dimen.size_30dp
                )
                itemView.setMargin(
                    bottom = CommonUtils.getNavigationBarHeight(context) + space
                )
            } else {
                itemView.setMargin(
                    bottom = 0
                )
            }
            with(binding) {
                tvTitle.text = itemView.context.getString(history.barcodeType.typeNameResId)
                tvDescription.text = getDescription(barcodeData = history.barcodeData)
                tvCreateAt.text = history.createAt
                imgIcon.setImageResource(history.barcodeType.resIcon)
            }
        }

        private fun getDescription(barcodeData: BarcodeData): String {
            return when (barcodeData) {
                is TextBarcode -> barcodeData.value
                is PhoneBarcode -> barcodeData.phoneNumber
                is WifiBarcode -> "${barcodeData.ssid} - ${barcodeData.password}"
                is UrlBarcode -> barcodeData.url
                is ContactBarcode -> "${barcodeData.firstName} - ${barcodeData.lastName} - ${barcodeData.phoneNumber}"
                is EmailBarcode -> barcodeData.email
                is SmsBarcode -> "${barcodeData.message} - ${barcodeData.phoneNumber}"
                else -> ""
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