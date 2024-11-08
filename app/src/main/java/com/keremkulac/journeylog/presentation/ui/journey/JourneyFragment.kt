package com.keremkulac.journeylog.presentation.ui.journey


import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentJourneyBinding


class JourneyFragment : BaseFragment<FragmentJourneyBinding>(FragmentJourneyBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createRoute()
    }

    private fun createRoute(){
        binding.createRoute.setOnClickListener {
            findNavController().navigate(R.id.action_journeyFragment_to_mapFragment)
        }
    }
}