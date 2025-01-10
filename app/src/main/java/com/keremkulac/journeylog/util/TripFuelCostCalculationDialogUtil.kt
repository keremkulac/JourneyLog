package com.keremkulac.journeylog.util


import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.DialogTripFuelCostCalculationBinding
import com.keremkulac.journeylog.domain.model.AverageFuelPrice
import com.keremkulac.journeylog.domain.model.Vehicle

class TripFuelCostCalculationDialogUtil(
    private val context: Context,
    private val averageFuelPriceList: List<AverageFuelPrice>,
    private val vehicleList: List<Vehicle>,
    private val inputValidation: InputValidation
) {
    private lateinit var binding: DialogTripFuelCostCalculationBinding
    private var selectedAverageFuel: AverageFuelPrice? = null
    private var selectedVehicle: Vehicle? = null
    private val dialog: Dialog = createDialog()
    private var selection = -1
    private var validationMessage = ""


    private fun createDialog(): Dialog {
        return Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            binding = DialogTripFuelCostCalculationBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setupWindow()
        }
    }

    private fun Dialog.setupWindow() {
        window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            decorView.setPadding(32, 32, 32, 32)
        }
    }

    fun showDialog() {
        setupLicensePlateSpinner()
        setupFuelTypeSpinner()
        otherVehicleSelection()
        ownVehicleSelection()
        cancelClick()
        forwardClick()
        calculateOwnTripCostFuelConsumption()
        calculateOtherTripCostFuelConsumption()
        dialog.show()
    }

    private fun setupFuelTypeSpinner() {
        with(binding.otherVehicleFuelType) {
            val adapter = ArrayAdapter(
                context,
                android.R.layout.simple_dropdown_item_1line,
                averageFuelPriceList.map { it.title }
            )
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                selectedAverageFuel = averageFuelPriceList[position]
            }
        }
    }

    private fun setupLicensePlateSpinner() {
        with(binding.licensePlate) {
            val adapter = ArrayAdapter(
                context,
                android.R.layout.simple_dropdown_item_1line,
                vehicleList.map { it.licensePlate }
            )
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                selectedVehicle = vehicleList[position]
            }
        }
    }

    private fun ownVehicleSelection() {
        with(binding) {
            forOwnVehicleCardView.setOnClickListener {
                selection = 0
                resetCardViewBorder()
                forOwnVehicleCardView.strokeColor = context.getColor(R.color.main)
            }
        }
    }

    private fun otherVehicleSelection() {
        with(binding) {
            forOtherVehicleCardView.setOnClickListener {
                selection = 1
                resetCardViewBorder()
                forOtherVehicleCardView.strokeColor = context.getColor(R.color.main)
            }
        }

    }

    private fun selection() {
        when (selection) {
            0 -> ownVehicleSelect()
            1 -> otherVehicleSelect()
        }
    }

    private fun ownVehicleSelect() {
        with(binding) {
            mainSelectionLayout.visibility = View.GONE
            ownVehicleSelectionLayout.visibility = View.VISIBLE
        }
    }

    private fun otherVehicleSelect() {
        with(binding) {
            mainSelectionLayout.visibility = View.GONE
            otherVehicleSelectionLayout.visibility = View.VISIBLE
        }
    }

    private fun ownVehicleCalculation() {
        with(binding) {
            mainSelectionLayout.visibility = View.GONE
            ownVehicleSelectionLayout.visibility = View.GONE
            messageLayout.visibility = View.VISIBLE
            calculationAnimation.playAnimation()
        }
    }

    private fun otherVehicleCalculation() {
        with(binding) {
            mainSelectionLayout.visibility = View.GONE
            otherVehicleSelectionLayout.visibility = View.GONE
            messageLayout.visibility = View.VISIBLE
            calculationAnimation.playAnimation()
        }
    }

    private fun cancelClick() {
        binding.cancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun forwardClick() {
        binding.forward.setOnClickListener {
            selection()
        }
    }

    private fun resetCardViewBorder() {
        with(binding) {
            forOwnVehicleCardView.strokeColor = context.getColor(R.color.border)
            forOtherVehicleCardView.strokeColor = context.getColor(R.color.border)
        }
    }

    private fun calculateOwnTripCostFuelConsumption() {
        with(binding) {
            calculateOwnTripCostFuelConsumption.setOnClickListener {
                val distance = ownVehicleDistanceToTrip.text.toString()
                if (!validateOwnTripCostFuelCalculation(selectedVehicle, distance)) {
                    Toast.makeText(context, validationMessage, Toast.LENGTH_SHORT).show()
                } else {
                    val totalPrice = ownVehicleDistanceToTrip.text.toString()
                        .toDouble() * selectedVehicle!!.perKilometersFuelPrice!!.toDouble()
                    ownVehicleCalculation()
                    message.text = context.getString(R.string.dialog_trip_fuel_cost_message)
                        .format(distance, totalPrice.toMoneyFormat())
                }
            }
        }
    }

    private fun calculateOtherTripCostFuelConsumption() {
        with(binding) {
            calculateOtherTripCostFuelConsumption.setOnClickListener {
                val distance = otherVehicleDistanceToTrip.text.toString()
                val otherVehicleUsedFuelPer100Kilometers = otherVehicleUsedFuelPer100Kilometers.text.toString()
                if (!validateOtherTripCostFuelCalculation(selectedAverageFuel,distance,otherVehicleUsedFuelPer100Kilometers)){
                    Toast.makeText(context, validationMessage, Toast.LENGTH_SHORT).show()
                }else{
                    val totalPrice = distance.toDouble() * ((selectedAverageFuel!!.value.toDouble() * otherVehicleUsedFuelPer100Kilometers
                        .toDouble()) / 100)
                    otherVehicleCalculation()
                    message.text = context.getString(R.string.dialog_trip_fuel_cost_message)
                        .format(distance, totalPrice.toMoneyFormat())
                }

            }
        }
    }

    private fun validateOwnTripCostFuelCalculation(
        selectedVehicle: Vehicle?,
        distanceToTrip: String?,
    ): Boolean {
        return inputValidation.validateOwnTripCostFuelCalculation(
            selectedVehicle,
            distanceToTrip
        ) { message ->
            validationMessage = message
        }
    }

    private fun validateOtherTripCostFuelCalculation(
        averageFuelPrice: AverageFuelPrice?,
        distanceToTrip: String?,
        vehicleUsedFuelPer100Kilometers: String?
    ): Boolean {
        return inputValidation.validateOtherTripCostFuelCalculation(
            averageFuelPrice,
            distanceToTrip,
            vehicleUsedFuelPer100Kilometers
        ) { message ->
            validationMessage = message
        }
    }

}
