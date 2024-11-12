package com.keremkulac.journeylog.presentation.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.databinding.FragmentHomeBinding
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fuelPrices.observe(viewLifecycleOwner){fuelPrices->
            Log.d("TAG",fuelPrices.toString())
            when(fuelPrices){
                is Result.Failure -> TODO()
                Result.Loading -> TODO()
                is Result.Success -> Log.d("TAG",fuelPrices.toString())
            }
        }
    }
}