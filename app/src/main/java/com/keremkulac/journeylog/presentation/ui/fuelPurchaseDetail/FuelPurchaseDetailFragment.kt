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
    private lateinit var detailLayoutManager: ExpandableLayoutManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillFields()
        setupExpandableLayouts()
        showDetails()
    }

    private fun fillFields() {
        val receipt = args.receipt
        receipt?.let {
            binding.apply {
                stationName.text = receipt.stationName
                fuelType.text = translationHelper.translate(
                    receipt.fuelType,
                    TranslationHelper.TranslationType.Fuel
                )
                requireContext().apply {
                    literPrice.text =
                        getString(R.string.fuel_liter_price).format(receipt.literPrice)
                    liter.text = getString(R.string.liters_taken).format(receipt.liter)
                    receiptTotalPrice.text = getString(R.string.total_price).format(
                        receipt.total.toDouble().toMoneyFormat()
                    )
                    receiptTotalTax.text = getString(R.string.total_price).format(
                        receipt.tax.toDouble().toMoneyFormat()
                    )
                }
                vehicleLicensePlate.text = receipt.vehicleLicensePlate
                vehicleKm.text = receipt.vehicleLastKm.decimalFormat()
                time.text = receipt.time
                date.text = receipt.date
                totalPrice.text =
                    getString(R.string.total_price).format(receipt.total.toDouble().toMoneyFormat())
            }
        }
    }

    private fun setupExpandableLayouts() {
        with(binding) {
            fuelLayoutManager = ExpandableLayoutManager()
            vehicleLayoutManager = ExpandableLayoutManager()
            paymentLayoutManager = ExpandableLayoutManager()

            fuelCard.setOnClickListener {
                fuelLayoutManager.toggleLayout(
                    fuelCardLayout,
                    fuelCardOpen
                )
            }
            vehicleCard.setOnClickListener {
                vehicleLayoutManager.toggleLayout(
                    vehicleCardLayout,
                    vehicleCardOpen
                )
            }
            paymentCard.setOnClickListener {
                paymentLayoutManager.toggleLayout(
                    paymentCardLayout,
                    paymentCardOpen
                )
            }
        }
    }

    private fun showDetails() {
        with(binding) {
            detailLayoutManager = ExpandableLayoutManager()
            showDetails.setOnClickListener {
                detailLayoutManager.toggleLayout(detailLayout, detailCardOpen)
                detailLayoutManager.changeToggleText(
                    toggleText,
                    getString(R.string.fuel_purchase_detail_detail_toggle_open_text),
                    getString(R.string.fuel_purchase_detail_detail_toggle_close_text)
                )
                fuelCard.visibility = View.VISIBLE
                vehicleCard.visibility = View.VISIBLE
                paymentCard.visibility = View.VISIBLE
            }
        }
    }


}