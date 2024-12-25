package com.keremkulac.journeylog.presentation.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.domain.model.AverageFuelPrice
import com.keremkulac.journeylog.util.TranslationHelper
import javax.inject.Inject

class AverageFuelPriceAdapter @Inject constructor(val translationHelper: TranslationHelper): RecyclerView.Adapter<AverageFuelPriceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AverageFuelPriceAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_average_fuel_price, parent, false)
        )
    }

    private val diffUtil = object : DiffUtil.ItemCallback<AverageFuelPrice>() {
        override fun areItemsTheSame(
            oldItem: AverageFuelPrice,
            newItem: AverageFuelPrice
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: AverageFuelPrice,
            newItem: AverageFuelPrice
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)
    var averageFuelPriceList: ArrayList<AverageFuelPrice>
        get() = recyclerListDiffer.currentList.toMutableList() as ArrayList<AverageFuelPrice>
        set(value) {
            recyclerListDiffer.submitList(value as List<AverageFuelPrice?>?)
        }

    override fun onBindViewHolder(holder: AverageFuelPriceAdapter.ViewHolder, position: Int) {
        holder.bindItems(averageFuelPriceList[position])
    }

    override fun getItemCount(): Int {
        return averageFuelPriceList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(averageFuelPrice: AverageFuelPrice) {
            val fuelPriceTitle = itemView.findViewById<TextView>(R.id.fuelPriceTitle)
            val fuelPrice = itemView.findViewById<TextView>(R.id.fuelPrice)
            fuelPriceTitle.text = translationHelper.translateManually(averageFuelPrice.title)
            fuelPrice.text = averageFuelPrice.value
        }
    }
}