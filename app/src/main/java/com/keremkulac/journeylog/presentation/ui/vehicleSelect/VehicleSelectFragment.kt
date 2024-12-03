package com.keremkulac.journeylog.presentation.ui.vehicleSelect

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentVehicleSelectBinding
import com.keremkulac.journeylog.domain.model.Vehicle
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class VehicleSelectFragment : BottomSheetDialogFragment(R.layout.fragment_vehicle_select) {

    private lateinit var binding: FragmentVehicleSelectBinding
    private lateinit var adapter: VehicleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVehicleSelectBinding.bind(view)
        createRecyclerView()
        vehicleClick()
    }

    private fun createRecyclerView() {
        adapter = VehicleAdapter()
        val vehicleItems = listOf(
            Vehicle(R.drawable.ic_bike, "Motorsiklet"),
            Vehicle(R.drawable.ic_car, "Otomobil"),
            Vehicle(R.drawable.ic_suv, "SUV"),
            Vehicle(R.drawable.ic_van, "Ticari"),
            Vehicle(R.drawable.ic_truck, "Kamyonet")
        )
        binding.selectVehicleRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.vehicleList = ArrayList(vehicleItems)
        binding.selectVehicleRecyclerView.adapter = adapter
    }

    private fun vehicleClick() {
        adapter.clickListener = { vehicle ->
            binding.continueLicensePlate.visibility = View.VISIBLE
        }
    }
}