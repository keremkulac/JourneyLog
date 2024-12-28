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
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentVehicleCreateBinding
import com.keremkulac.journeylog.domain.model.AverageFuelPrice
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.CustomDialog
import com.keremkulac.journeylog.util.HandleResult
import com.keremkulac.journeylog.util.SharedViewModel
import com.keremkulac.journeylog.util.TranslationHelper
import com.keremkulac.journeylog.util.VehicleItemsUtil
import com.keremkulac.journeylog.util.decimalFormat
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class VehicleCreateFragment :
    BaseFragment<FragmentVehicleCreateBinding>(FragmentVehicleCreateBinding::inflate) {

    private lateinit var sharedViewModel: SharedViewModel

    @Inject
    lateinit var translationHelper: TranslationHelper
    private var selectedVehicle: Vehicle? = null
    private var user: User? = null
    private var selectedFuelType: String? = null
    private var selectedVehicleType: String? = null
    private val viewModel by viewModels<VehicleCreateViewModel>()
    private val args by navArgs<VehicleCreateFragmentArgs>()
    private lateinit var averageFuelPriceList: List<AverageFuelPrice>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        createVehicleType()
        createFuelType()
        observeUser()
        observeValidation()
        createVehicle()
        observeSaveVehicleResult()
        selectFuelType()
        selectVehicleType()
        observeAverageFuelPrices()
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

    private fun createVehicleType() {
        val adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.item_dropdown,
                resources.getStringArray(R.array.vehicleTypes)
            )
        binding.vehicleTypeSelect.setAdapter(adapter)
    }

    private fun createVehicle() {
        binding.confirmVehicle.setOnClickListener {
            val licensePlate =
                binding.vehicleLicensePlate.text.toString().uppercase(Locale.getDefault())
            val lastKm = binding.vehicleLastKm.text.toString()
            val per100KilometerFuel = binding.per100KilometerFuel.text.toString()
            if (viewModel.validateLicensePlate(
                    selectedVehicleType,
                    licensePlate,
                    lastKm,
                    selectedFuelType,
                    per100KilometerFuel
                )
            ) {
                user?.let {
                    val fuelCost = calculateFuelCost(averageFuelPriceList, per100KilometerFuel)
                    selectedVehicle = VehicleItemsUtil.getVehicleItems().find { it.title == selectedVehicleType }
                    selectedVehicle?.let { vehicle ->
                        vehicle.id = UUID.randomUUID().toString()
                        vehicle.userId = it.id
                        vehicle.licensePlate = licensePlate
                        vehicle.lastKm = lastKm
                        vehicle.title = selectedVehicleType
                        vehicle.vehicleFuelType = selectedFuelType
                        vehicle.per100KilometersFuelLiter = per100KilometerFuel
                        vehicle.perKilometersFuelPrice = (fuelCost.toDouble() / 100).toString().decimalFormat()
                        vehicle.per100KilometersFuelPrice = fuelCost
                        showDialog(vehicle)
                    }
                }
            }
        }
    }

    private fun calculateFuelCost(
        averageFuelPriceList: List<AverageFuelPrice>,
        per100KilometerFuel: String
    ): String {
        var per100KilometerInfo = 0.0
        averageFuelPriceList.find { it.title == selectedFuelType }?.let { fuelPrice ->
            per100KilometerInfo = per100KilometerFuel.toDouble() * fuelPrice.value.toDouble()
        }
        return per100KilometerInfo.toString().decimalFormat()
    }

    private fun observeAverageFuelPrices() {
        viewModel.averageFuelPrices.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result, onSuccess = { data ->
                val averageFuelPriceList = data as List<AverageFuelPrice>
                this.averageFuelPriceList = averageFuelPriceList
            })
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

    private fun selectFuelType() {
        binding.vehicleFuelType.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                selectedFuelType = parent.getItemAtPosition(position).toString()
            }
    }

    private fun selectVehicleType() {
        binding.vehicleTypeSelect.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                selectedVehicleType = parent.getItemAtPosition(position).toString()
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