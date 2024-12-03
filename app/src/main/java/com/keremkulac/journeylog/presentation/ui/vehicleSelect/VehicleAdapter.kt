package com.keremkulac.journeylog.presentation.ui.vehicleSelect

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

class VehicleAdapter : RecyclerView.Adapter<VehicleAdapter.ViewHolder>() {

    var clickListener: ((Vehicle) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_select_vehicle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(vehicleList[position])
        holder.itemView.setOnClickListener{
            clickListener?.invoke(vehicleList[position])
        }
    }

    override fun getItemCount() = vehicleList.size

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


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(vehicle: Vehicle) {
            val icon: ImageView = itemView.findViewById(R.id.vehicleIcon)
            val title: TextView = itemView.findViewById(R.id.vehicleName)
            icon.setImageResource(vehicle.iconResId!!)
            title.text = vehicle.title
        }

    }

}
