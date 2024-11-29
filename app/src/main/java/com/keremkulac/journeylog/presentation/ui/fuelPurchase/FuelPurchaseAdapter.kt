package com.keremkulac.journeylog.presentation.ui.fuelPurchase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.domain.model.Receipt

class FuelPurchaseAdapter : RecyclerView.Adapter<FuelPurchaseAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FuelPurchaseAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_fuel_purchase, parent, false)
        )
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Receipt>() {
        override fun areItemsTheSame(
            oldItem: Receipt,
            newItem: Receipt
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Receipt,
            newItem: Receipt
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)
    var receiptList: ArrayList<Receipt>
        get() = recyclerListDiffer.currentList.toMutableList() as ArrayList<Receipt>
        set(value) {
            recyclerListDiffer.submitList(value as List<Receipt?>?)
        }

    override fun onBindViewHolder(holder: FuelPurchaseAdapter.ViewHolder, position: Int) {
        holder.bindItems(receiptList[position])
    }

    override fun getItemCount(): Int {
        return receiptList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(receipt: Receipt) {
            val fuelPriceTitle = itemView.findViewById<TextView>(R.id.stationName)
            val fuelPrice = itemView.findViewById<TextView>(R.id.fuelType)
            val date = itemView.findViewById<TextView>(R.id.date)
            val liter = itemView.findViewById<TextView>(R.id.liter)
            val literPrice = itemView.findViewById<TextView>(R.id.literPrice)
            val totalPrice = itemView.findViewById<TextView>(R.id.totalPrice)

            fuelPriceTitle.text = receipt.stationName
            fuelPrice.text = receipt.fuelType
            date.text = receipt.date
            liter.text = receipt.liter
            literPrice.text = receipt.literPrice
            totalPrice.text = receipt.total

        }
    }
}