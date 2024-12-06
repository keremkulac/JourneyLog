package com.keremkulac.journeylog.presentation.ui.vehicleView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.domain.model.Vehicle

class VehicleViewAdapter : RecyclerView.Adapter<VehicleViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VehicleViewAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_all_vehicle, parent, false)
        )
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Vehicle>() {
        override fun areItemsTheSame(
            oldItem: Vehicle,
            newItem: Vehicle
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Vehicle,
            newItem: Vehicle
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)
    var vehicleList: ArrayList<Vehicle>
        get() = recyclerListDiffer.currentList.toMutableList() as ArrayList<Vehicle>
        set(value) {
            recyclerListDiffer.submitList(value as List<Vehicle?>?)
        }

    override fun onBindViewHolder(holder: VehicleViewAdapter.ViewHolder, position: Int) {
        holder.binds(vehicleList[position])
    }

    override fun getItemCount(): Int {
        return vehicleList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun binds(vehicle: Vehicle) {
            val icon: ImageView = itemView.findViewById(R.id.vehicleIcon)
            val title: TextView = itemView.findViewById(R.id.vehicleTitle)
            val licensePlate : TextView = itemView.findViewById(R.id.licensePlate)
            icon.setImageResource(vehicle.iconResId!!)
            title.text = vehicle.title
            licensePlate.text = vehicle.licensePlate
        }
    }
}