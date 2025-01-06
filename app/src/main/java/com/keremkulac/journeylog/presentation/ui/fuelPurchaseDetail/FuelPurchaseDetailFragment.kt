package com.keremkulac.journeylog.presentation.ui.fuelPurchaseDetail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentFuelPurchaseDetailBinding
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.ExpandableLayoutManager
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
    private lateinit var fuelLayoutManager: ExpandableLayoutManager
    private lateinit var vehicleLayoutManager: ExpandableLayoutManager
    private lateinit var paymentLayoutManager: ExpandableLayoutManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillFields()
        setupExpandableLayouts()
    }

    private fun fillFields() {
        val receipt = args.receipt
        receipt?.let {
            binding.apply {
                stationName.text = receipt.stationName
                fuelType.text = translationHelper.translate(receipt.fuelType, TranslationHelper.TranslationType.Fuel)
                requireContext().apply {
                    literPrice.text = getString(R.string.fuel_liter_price).format(receipt.literPrice)
                    liter.text = getString(R.string.liters_taken).format(receipt.liter)
                    receiptTotalPrice.text = getString(R.string.total_price).format(receipt.total.toDouble().toMoneyFormat())
                    receiptTotalTax.text = getString(R.string.total_price).format(receipt.tax.toDouble().toMoneyFormat())
                }
                vehicleLicensePlate.text = receipt.vehicleLicensePlate
                vehicleKm.text = receipt.vehicleLastKm.decimalFormat()
                //date.text = receipt.date
            }

        }
    }

    private fun setupExpandableLayouts() {
        binding.apply {
            fuelLayoutManager = ExpandableLayoutManager(fuelCardOpen)
            vehicleLayoutManager = ExpandableLayoutManager(vehicleCardOpen)
            paymentLayoutManager = ExpandableLayoutManager(paymentCardOpen)

            fuelCard.setOnClickListener { fuelLayoutManager.toggleLayout(fuelCardLayout) }
            vehicleCard.setOnClickListener { vehicleLayoutManager.toggleLayout(vehicleCardLayout) }
            paymentCard.setOnClickListener { paymentLayoutManager.toggleLayout(paymentCardLayout) }
        }
    }


}