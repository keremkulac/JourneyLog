package com.keremkulac.journeylog.presentation.ui.fuelPurchaseDetail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentFuelPurchaseDetailBinding
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.TranslationHelper
import com.keremkulac.journeylog.util.decimalFormat
import com.keremkulac.journeylog.util.toMoneyFormat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FuelPurchaseDetailFragment :
    BaseFragment<FragmentFuelPurchaseDetailBinding>(FragmentFuelPurchaseDetailBinding::inflate) {

    @Inject
    lateinit var translationHelper: TranslationHelper
    private val args: FuelPurchaseDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillFields()
    }

    private fun fillFields() {
        val receipt = args.receipt
        receipt?.let {
            binding.stationName.text = receipt.stationName
            binding.fuelType.text = translationHelper.translate(receipt.fuelType, TranslationHelper.TranslationType.Fuel)
            requireContext().apply {
                binding.literPrice.text = getString(R.string.fuel_liter_price).format(receipt.literPrice)
                binding.liter.text = getString(R.string.liters_taken).format(receipt.liter)
                binding.receiptTotalPrice.text = getString(R.string.total_price).format(receipt.total.toDouble().toMoneyFormat())
                binding.receiptTotalTax.text = getString(R.string.total_price).format(receipt.tax.toDouble().toMoneyFormat())
            }
            binding.vehicleLicensePlate.text = receipt.vehicleLicensePlate
            binding.vehicleKm.text = receipt.vehicleLastKm.decimalFormat()
            //   binding.date.text = receipt.date

        }
    }

}