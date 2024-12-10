package com.keremkulac.journeylog.presentation.ui.vehicleDetail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.keremkulac.journeylog.databinding.FragmentVehicleDetailBinding
import com.keremkulac.journeylog.util.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VehicleDetailFragment :
    BaseFragment<FragmentVehicleDetailBinding>(FragmentVehicleDetailBinding::inflate) {

    private val args: VehicleDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillVehicleDetail()
    }

    private fun fillVehicleDetail() {
        val vehicle = args.vehicle
        vehicle?.let {
            binding.vehicleTitle.text = it.title
            binding.licensePlate.text = it.licensePlate
            binding.vehicleIcon.setImageResource(it.iconResId!!)
        }
    }

}