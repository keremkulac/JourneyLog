package com.keremkulac.journeylog.presentation.ui.fuelPurchaseView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.util.TranslationHelper
import javax.inject.Inject

class FuelPurchaseViewAdapter @Inject constructor(val translationHelper: TranslationHelper) :
    RecyclerView.Adapter<FuelPurchaseViewAdapter.ViewHolder>() {
    var clickListener: ((Receipt) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FuelPurchaseViewAdapter.ViewHolder {
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

    override fun onBindViewHolder(holder: FuelPurchaseViewAdapter.ViewHolder, position: Int) {
        holder.bindItems(receiptList[position])
    }

    override fun getItemCount(): Int {
        return receiptList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(receipt: Receipt) {
            val fuelTotalPrice = itemView.findViewById<TextView>(R.id.fuelTotalPrice)
            val fuelType = itemView.findViewById<TextView>(R.id.fuelType)
            val date = itemView.findViewById<TextView>(R.id.date)
            val cardView = itemView.findViewById<CardView>(R.id.cardView)
            itemView.context.apply {
                fuelTotalPrice.text = getString(R.string.total_price).format(receipt.total)
                fuelType.text = translationHelper.translateManually(receipt.fuelType)
                date.text = receipt.date
            }
            cardView.setOnClickListener { clickListener?.invoke(receipt) }
        }
    }

    fun filterList(filterList: ArrayList<Receipt>) {
        receiptList = filterList
        notifyDataSetChanged()
    }
}