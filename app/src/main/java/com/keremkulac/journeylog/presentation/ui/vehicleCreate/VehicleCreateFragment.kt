package com.keremkulac.journeylog.presentation.ui.vehicleCreate

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentVehicleCreateBinding
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.CustomDialog
import com.keremkulac.journeylog.util.HandleResult
import com.keremkulac.journeylog.util.SharedViewModel
import com.keremkulac.journeylog.util.VehicleItemsUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import java.util.Locale

@AndroidEntryPoint
class VehicleCreateFragment :
    BaseFragment<FragmentVehicleCreateBinding>(FragmentVehicleCreateBinding::inflate) {

    private lateinit var adapter: VehicleCreateAdapter
    private lateinit var sharedViewModel: SharedViewModel
    private var selectedVehicle: Vehicle? = null
    private var user: User? = null
    private var selectedFuelType : String? = null
    private val viewModel by viewModels<VehicleCreateViewModel>()
    private val args by navArgs<VehicleCreateFragmentArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        observeUser()
        createRecyclerView()
        vehicleClick()
        observeValidation()
        createVehicle()
        observeSaveVehicleResult()
        createFuelType()
        selectFuelType()
    }


    private fun createRecyclerView() {
        adapter = VehicleCreateAdapter()
        binding.selectVehicleRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.vehicleList = ArrayList(VehicleItemsUtil.getVehicleItems())
        binding.selectVehicleRecyclerView.adapter = adapter
    }

    private fun vehicleClick() {
        adapter.clickListener = { vehicle ->
            selectedVehicle = vehicle
        }
    }

    private fun createFuelType() {
        val adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.item_dropdown,
                resources.getStringArray(R.array.fuelTypes)
            )
        binding.vehicleFuelType.setAdapter(adapter)
    }

    private fun createVehicle() {
        binding.confirmVehicle.setOnClickListener {
            val licensePlate = binding.vehicleLicensePlate.text.toString().uppercase(Locale.getDefault())
            val lastKm = binding.vehicleLastKm.text.toString()
            if (viewModel.validateLicensePlate(selectedVehicle, licensePlate, lastKm,selectedFuelType)) {
                user?.let {
                    selectedVehicle?.let { vehicle ->
                        vehicle.userId = it.id
                        vehicle.licensePlate = licensePlate
                        vehicle.lastKm = lastKm
                        vehicle.vehicleFuelType = selectedFuelType
                        showDialog(vehicle)
                    }
                }
            }
        }
    }

    private fun observeValidation() {
        viewModel.validationMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeUser() {
        sharedViewModel.sharedData.observe(viewLifecycleOwner) { user ->
            this.user = user
        }
    }

    private fun observeSaveVehicleResult() {
        viewModel.saveVehicleResult.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result,
                onSuccess = { data ->
                    Toast.makeText(requireContext(), data, Toast.LENGTH_SHORT).show()
                }, onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun selectFuelType(){
        binding.vehicleFuelType.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                selectedFuelType = parent.getItemAtPosition(position).toString()
            }
    }

    private fun showDialog(vehicle: Vehicle) {
        requireContext().apply {
            CustomDialog.showConfirmationDialog(
                this,
                getString(R.string.dialog_save_vehicle_title),
                getString(R.string.dialog_save_vehicle_message),
                getString(R.string.dialog_save_vehicle_positive_button_text),
                getString(R.string.dialog_save_vehicle_negative_button_text)
            ) {
                viewModel.saveVehicle(vehicle)
                if (args.fromAddPurchase) {
                    findNavController().navigate(R.id.action_vehicleCreateFragment_to_fuelPurchaseAddFragment)
                } else {
                    findNavController().navigate(R.id.action_vehicleCreateFragment_to_vehicleViewFragment)
                }
            }
        }
    }


}