package com.keremkulac.journeylog.util

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.DialogFuelConsumptionBinding
import com.keremkulac.journeylog.domain.model.AverageFuelPrice

class FuelConsumptionDialogUtil(
    private val context: Context,
    private val averageFuelPriceList: List<AverageFuelPrice>,
    private val inputValidation: InputValidation
) {
    private lateinit var binding: DialogFuelConsumptionBinding
    private var selectedFuelType: String = ""
    private val dialog: Dialog = createDialog()
    private var validationMessage = ""

    private fun createDialog(): Dialog {
        return Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            binding = DialogFuelConsumptionBinding.inflate(layoutInflater)
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
        setupFuelTypeSpinner()
        setupCalculateButton()
        dialog.show()
    }

    private fun setupFuelTypeSpinner() {
        with(binding.fuelType) {
            val adapter = ArrayAdapter(
                context,
                android.R.layout.simple_dropdown_item_1line,
                averageFuelPriceList.map { it.title }
            )
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                selectedFuelType = averageFuelPriceList[position].title
            }
        }
    }

    private fun setupCalculateButton() {
        with(binding) {
            calculateButton.setOnClickListener {
                if (!validateFuelConsumption(kmLiter.text.toString(), selectedFuelType)) {
                    Toast.makeText(context, validationMessage, Toast.LENGTH_SHORT).show()
                } else {
                    calculateButton.hideKeyboard()
                    calculateConsumption()
                }
            }
        }

    }

    private fun calculateConsumption() {
        val literAmount = binding.kmLiter.text.toString().toDoubleOrNull() ?: return
        averageFuelPriceList.find { it.title == selectedFuelType }?.let { fuelPrice ->
            val per100KilometerInfo = literAmount * fuelPrice.value.toDouble()
            val perKilometerInfo = per100KilometerInfo / 100
            showResults(perKilometerInfo, per100KilometerInfo)
        }
    }

    private fun showResults(perKilometer: Double, per100Kilometer: Double) {
        with(binding) {
            calculateLayout.visibility = View.GONE
            messageLayout.visibility = View.VISIBLE
            perKilometerPriceMessage.text =
                context.getString(R.string.fuel_consumption_per_kilometer_info)
                    .format(perKilometer.toMoneyFormat())
            per100KilometerPriceMessage.text =
                context.getString(R.string.fuel_consumption_per_100_kilometers_info)
                    .format(per100Kilometer.toMoneyFormat())
        }
    }

    private fun validateFuelConsumption(
        per100KilometerFuel: String?,
        selectedFuelType: String?
    ): Boolean {
        return inputValidation.validateFuelConsumption(
            per100KilometerFuel,
            selectedFuelType
        ) { message ->
            validationMessage = message
        }

    }
}
