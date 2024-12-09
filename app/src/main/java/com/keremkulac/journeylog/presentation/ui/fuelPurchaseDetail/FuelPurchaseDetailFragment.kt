package com.keremkulac.journeylog.presentation.ui.fuelPurchaseDetail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentFuelPurchaseDetailBinding
import com.keremkulac.journeylog.util.BaseFragment


class FuelPurchaseDetailFragment :
    BaseFragment<FragmentFuelPurchaseDetailBinding>(FragmentFuelPurchaseDetailBinding::inflate) {

    private val args: FuelPurchaseDetailFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillFields()
    }

    private fun fillFields() {
        val receipt = args.receipt
        receipt?.let {
            binding.stationName.text = receipt.stationName
            requireContext().apply {
                binding.fuelTypeTitle.text = receipt.fuelType
                binding.fuelLiterPrice.text =
                    getString(R.string.fuel_liter_price).format(receipt.literPrice)
                binding.liter.text = getString(R.string.liters_taken).format(receipt.liter)
                binding.totalPrice.text = getString(R.string.total_price).format(receipt.total)
            }
            binding.date.text = receipt.date

        }
    }

}