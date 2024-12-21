package com.keremkulac.journeylog.presentation.ui.vehicleView

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentVehicleViewBinding
import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.HandleResult
import com.keremkulac.journeylog.util.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class VehicleViewFragment :
    BaseFragment<FragmentVehicleViewBinding>(FragmentVehicleViewBinding::inflate) {

    private lateinit var sharedViewModel: SharedViewModel
    private val viewModel by viewModels<VehicleViewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        observeUser()
        observeAllVehicles()
        navigateVehicleCreate()
    }

    private fun observeAllVehicles() {
        viewModel.getAllVehicles.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result,
                onSuccess = { data ->
                    val vehicleList = data as List<Vehicle>
                    setRecyclerView(vehicleList)
                    checkEmptyList(vehicleList)
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
        clickVehicle(adapter)
    }

    private fun checkEmptyList(vehicleList: List<Vehicle>) {
        if (vehicleList.isEmpty()) {
            binding.vehiclesRecyclerView.visibility = View.GONE
            binding.emptyWarning.visibility = View.VISIBLE
        } else {
            binding.vehiclesRecyclerView.visibility = View.VISIBLE
            binding.emptyWarning.visibility = View.GONE
            binding.vehicleCardInfo.text = getString(R.string.vehicle_view_card_info_text).format(vehicleList.size)
        }
    }



    private fun navigateVehicleCreate() {
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_vehicleViewFragment_to_vehicleCreateFragment)
        }
    }

    private fun clickVehicle(adapter: VehicleViewAdapter) {
        adapter.clickListener = { vehicle ->
            findNavController().navigate(
                VehicleViewFragmentDirections.actionVehicleViewFragmentToVehicleDetailFragment(
                    vehicle
                )
            )
        }
    }
}