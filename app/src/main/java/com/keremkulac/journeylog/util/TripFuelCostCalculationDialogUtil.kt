package com.keremkulac.journeylog.util


import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.DialogTripFuelCostCalculationBinding
import com.keremkulac.journeylog.domain.model.AverageFuelPrice
import com.keremkulac.journeylog.domain.model.Vehicle

class TripFuelCostCalculationDialogUtil(
    private val context: Context,
    private val averageFuelPriceList: List<AverageFuelPrice>,
    private val vehicleList: List<Vehicle>
) {
    private lateinit var binding: DialogTripFuelCostCalculationBinding
    private var selectedFuelType: String = ""
    private var selectedLicensePlate: String = ""
    private val dialog: Dialog = createDialog()
    private var selection = -1

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
                selectedFuelType = averageFuelPriceList[position].title
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
                selectedLicensePlate = averageFuelPriceList[position].title
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

}
