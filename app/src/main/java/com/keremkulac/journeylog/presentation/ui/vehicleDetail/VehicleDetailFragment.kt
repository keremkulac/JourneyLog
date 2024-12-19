package com.keremkulac.journeylog.presentation.ui.vehicleDetail

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.navArgs
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentVehicleDetailBinding
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.ResourceUtil
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
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_vehicle_illustration)
            binding.vehicleIllustration.startAnimation(animation)
            binding.vehicleIllustration.setImageResource(ResourceUtil.getResourceId(resources,it.illustrationName!!,requireContext().packageName))
            binding.vehicleTitle.text = it.title
            binding.licensePlate.text = it.licensePlate
            binding.kilometerTitle.text = it.lastKm
            binding.vehicleIcon.setImageResource(ResourceUtil.getResourceId(resources,it.iconName!!,requireContext().packageName))
        }
    }

}