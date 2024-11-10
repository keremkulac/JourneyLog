package com.keremkulac.journeylog.presentation.ui.fuelPurchase


import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.databinding.FragmentFuelPurchaseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FuelPurchaseFragment : BaseFragment<FragmentFuelPurchaseBinding>(FragmentFuelPurchaseBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateAddFuelPurchase()
    }

    private fun navigateAddFuelPurchase(){
        binding.fab.setOnClickListener{
            findNavController().navigate(R.id.action_fuelPurchaseFragment_to_addFuelPurchaseFragment)
        }
    }
}