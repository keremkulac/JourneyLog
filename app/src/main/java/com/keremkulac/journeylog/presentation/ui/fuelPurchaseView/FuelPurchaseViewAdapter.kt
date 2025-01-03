package com.keremkulac.journeylog.presentation.ui.fuelPurchaseView

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.util.FuelType
import com.keremkulac.journeylog.util.TranslationHelper
import com.keremkulac.journeylog.util.toMoneyFormat
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
            val fuelIcon = itemView.findViewById<ImageView>(R.id.fuelİcon)
            itemView.context.apply {
                fuelTotalPrice.text =
                    getString(R.string.total_price).format(receipt.total.toDouble().toMoneyFormat())
                fuelType.text = translationHelper.translate(
                    receipt.fuelType,
                    TranslationHelper.TranslationType.Fuel
                )
                date.text = receipt.date
            }
            cardView.setOnClickListener { clickListener?.invoke(receipt) }
            fuelIcon.backgroundTintList = ColorStateList.valueOf(getFuelTypeColor(receipt.fuelType))
        }
    }

    fun filterList(filterList: ArrayList<Receipt>) {
        receiptList = filterList
        notifyDataSetChanged()
    }

    fun getFuelTypeColor(fuelType: String): Int {
        return when (fuelType) {
            FuelType.DIZEL.value -> Color.parseColor("#FFA600")
            FuelType.LPG.value -> Color.parseColor("#58508D")
            FuelType.BENZIN.value -> Color.parseColor("#FF6361")
            else -> {
                Color.parseColor("#FFFFFF")
            }
        }
    }
}