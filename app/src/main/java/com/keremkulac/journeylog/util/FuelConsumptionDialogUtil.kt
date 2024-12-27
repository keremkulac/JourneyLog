package com.keremkulac.journeylog.util

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.DialogFuelConsumptionBinding
import com.keremkulac.journeylog.domain.model.AverageFuelPrice

class FuelConsumptionDialogUtil(
    private val context: Context,
    private val averageFuelPriceList: List<AverageFuelPrice>
) {
    private lateinit var binding: DialogFuelConsumptionBinding
    private var selectedFuelType: String = ""
    private val dialog: Dialog = createDialog()

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
        }
    }

    fun showDialog() {
        with(binding) {
            setupFuelTypeSpinner()
            setupCalculateButton()
            dialog.show()
        }
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
        binding.calculateButton.setOnClickListener {
            calculateConsumption()
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
            listOf(binding.perKilometerCardView, binding.per100KilometerCardView).forEach {
                it.visibility = View.VISIBLE
            }

            perKilometerPrice.text = context.getString(R.string.total_price).format(perKilometer.toMoneyFormat())
            per100KilometerPrice.text = context.getString(R.string.total_price).format(per100Kilometer.toMoneyFormat())
        }
    }
}
