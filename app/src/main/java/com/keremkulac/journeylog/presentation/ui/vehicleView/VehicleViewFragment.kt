package com.keremkulac.journeylog.presentation.ui.vehicleView

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.journeylog.databinding.FragmentVehicleViewBinding
import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.HandleResult
import com.keremkulac.journeylog.util.SharedViewModel
import java.util.ArrayList


class VehicleViewFragment :
    BaseFragment<FragmentVehicleViewBinding>(FragmentVehicleViewBinding::inflate) {

    private lateinit var sharedViewModel: SharedViewModel
    private val viewModel by viewModels<VehicleViewViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        observeUser()
        observeAllVehicles()
    }

    private fun observeAllVehicles() {
        viewModel.getAllVehicles.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result,
                onSuccess = { data ->
                    val vehicleList = data as List<Vehicle>
                    setRecyclerView(vehicleList)
                }, onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun observeUser() {
        sharedViewModel.sharedData.observe(viewLifecycleOwner) { user ->
            viewModel.getAllVehicles(user.id)
        }
    }

    private fun setRecyclerView(vehicleList: List<Vehicle>) {
        val adapter = VehicleViewAdapter()
        binding.vehiclesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.vehicleList = ArrayList(vehicleList)
        binding.vehiclesRecyclerView.adapter = adapter
    }


}