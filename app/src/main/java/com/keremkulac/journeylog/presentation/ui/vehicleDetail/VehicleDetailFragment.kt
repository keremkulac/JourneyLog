package com.keremkulac.journeylog.presentation.ui.vehicleDetail

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentVehicleDetailBinding
import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.ResourceUtil
import com.keremkulac.journeylog.util.TranslationHelper
import com.keremkulac.journeylog.util.decimalFormat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VehicleDetailFragment :
    BaseFragment<FragmentVehicleDetailBinding>(FragmentVehicleDetailBinding::inflate) {

    @Inject
    lateinit var translationHelper: TranslationHelper
    private val args: VehicleDetailFragmentArgs by navArgs()
    private var vehicle : Vehicle? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillVehicleDetail()
        updateVehicle()
    }

    private fun fillVehicleDetail() {
        vehicle = args.vehicle
        vehicle?.let {
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_vehicle_illustration)
            binding.apply {
                vehicleIllustration.startAnimation(animation)
                vehicleIllustration.setImageResource(ResourceUtil.getResourceId(resources,it.illustrationName!!,requireContext().packageName))
                vehicleTitle.text = translationHelper.translate(it.title!!, TranslationHelper.TranslationType.Vehicle)
                licensePlate.text = it.licensePlate
                kilometerTitle.text = it.lastKm!!.decimalFormat()
                vehicleIcon.setImageResource(ResourceUtil.getResourceId(resources,it.iconName!!,requireContext().packageName))
            }
      }
    }

    private fun updateVehicle() {
        binding.vehicleUpdate.setOnClickListener {
            val bundle = Bundle().apply {
                putBoolean("fromVehicleDetail",true)
                putParcelable("vehicle",vehicle)
            }
            findNavController().navigate(R.id.action_vehicleDetailFragment_to_vehicleCreateFragment,bundle)
        }
    }

}