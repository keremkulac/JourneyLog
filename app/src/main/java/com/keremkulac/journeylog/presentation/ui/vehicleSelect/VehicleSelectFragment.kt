package com.keremkulac.journeylog.presentation.ui.vehicleSelect

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentVehicleSelectBinding
import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class VehicleSelectFragment :
    BaseFragment<FragmentVehicleSelectBinding>(FragmentVehicleSelectBinding::inflate) {

    private lateinit var adapter: VehicleAdapter
    private var selectedVehicle: Vehicle? = null
    private val viewModel by viewModels<VehicleSelectViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createRecyclerView()
        vehicleClick()
        observeValidation()
        licensesPlateInfo()
        createVehicle()
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
            selectedVehicle = vehicle
        }
    }

    private fun licensesPlateInfo() {
        binding.licensePlateInfoIcon.setOnClickListener {
            binding.licensePlateInfoText.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.licensePlateInfoText.visibility = View.GONE
            }, 2000)
        }

    }

    private fun createVehicle() {
        binding.confirmVehicle.setOnClickListener {
            val licensePlate = binding.licensePlate.text.toString()
            if(viewModel.validateLicensePlate(licensePlate, selectedVehicle)){ }
        }
    }


    private fun observeValidation() {
        viewModel.validationMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

}