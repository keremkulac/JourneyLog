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
            val resourceIconId = resources.getIdentifier(
                vehicle.iconName,
                "drawable",
                requireContext().packageName
            )
            val resourceIllustrationId = resources.getIdentifier(
                vehicle.illustrationName,
                "drawable",
                requireContext().packageName
            )
            binding.vehicleIllustration.setImageResource(resourceIllustrationId)
            binding.vehicleTitle.text = it.title
            binding.licensePlate.text = it.licensePlate
            binding.vehicleIcon.setImageResource(resourceIconId)
        }
    }

}