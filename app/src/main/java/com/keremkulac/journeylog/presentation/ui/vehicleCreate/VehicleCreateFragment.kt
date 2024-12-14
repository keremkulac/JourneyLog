package com.keremkulac.journeylog.presentation.ui.vehicleCreate

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentVehicleCreateBinding
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.CustomDialog
import com.keremkulac.journeylog.util.HandleResult
import com.keremkulac.journeylog.util.SharedViewModel
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
    private val viewModel by viewModels<VehicleCreateViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        observeUser()
        createRecyclerView()
        vehicleClick()
        observeValidation()
        licensesPlateInfo()
        createVehicle()
        observeSaveVehicleResult()
    }

    private fun createRecyclerView() {
        adapter = VehicleCreateAdapter()
        val vehicleItems = listOf(
            Vehicle(iconName = "ic_bike", title = "Motorsiklet"),
            Vehicle(iconName = "ic_car", title = "Otomobil"),
            Vehicle(iconName = "ic_suv", title = "SUV"),
            Vehicle(iconName = "ic_van", title = "Ticari"),
            Vehicle(iconName = "ic_truck", title = "Kamyonet")
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
            val licensePlate = binding.licensePlate.text.toString().uppercase(Locale.getDefault())
            if (viewModel.validateLicensePlate(licensePlate, selectedVehicle)) {
                user?.let {
                    selectedVehicle?.let { vehicle ->
                        vehicle.userId = it.id
                        vehicle.licensePlate = licensePlate
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
                findNavController().navigate(R.id.action_vehicleCreateFragment_to_vehicleViewFragment)
            }
        }
    }


}