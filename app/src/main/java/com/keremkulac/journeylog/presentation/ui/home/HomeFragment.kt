package com.keremkulac.journeylog.presentation.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.keremkulac.journeylog.data.local.model.AverageFuelPriceEntity
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.databinding.FragmentHomeBinding
import com.keremkulac.journeylog.domain.model.AverageFuelPrice
import com.keremkulac.journeylog.domain.model.DistrictData
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.util.FuelPriceOperations
import com.keremkulac.journeylog.util.FuelTypeTranslation
import com.keremkulac.journeylog.util.HandleResult
import com.keremkulac.journeylog.util.SharedViewModel
import com.keremkulac.journeylog.util.toAverageFuelPriceEntity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var sharedViewModel: SharedViewModel

    @Inject
    lateinit var fuelPriceOperations: FuelPriceOperations

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        onBackPressCancel()
        checkLastUpdate()
        getCurrentUser()
        observeFuelPrices()
        observeAverageFuelPrices()
    }

    private fun observeFuelPrices() {
        viewModel.fuelPrices.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result, onSuccess = { data ->
                viewModel.calculateAveragePrice(data as List<DistrictData>)
                viewModel.saveSharedPreferencesTime(System.currentTimeMillis())
            })
        }
    }

    private fun observeAverageFuelPrices() {
        viewModel.averageFuelPrices.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result, onSuccess = { data ->
                val averageFuelPriceList = ArrayList<AverageFuelPrice>()
                val averageFuelPriceEntityList = ArrayList<AverageFuelPriceEntity>()
                for (averagePrice in data) {
                    val averageFuelPrice = AverageFuelPrice(
                        FuelTypeTranslation.translateKey(averagePrice.key),
                        averagePrice.value
                    )
                    averageFuelPriceEntityList.add(averageFuelPrice.toAverageFuelPriceEntity())
                    averageFuelPriceList.add(averageFuelPrice)
                }
                viewModel.saveAverageFuelPrices(averageFuelPriceEntityList)
                setRecyclerView(averageFuelPriceList)
            })
        }
    }

    private fun setRecyclerView(averageFuelPriceList: ArrayList<AverageFuelPrice>) {
        val adapter = AverageFuelPriceAdapter()
        val pagerHelper = PagerSnapHelper()
        adapter.averageFuelPriceList = averageFuelPriceList
        binding.averageFuelPriceRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.averageFuelPriceRecyclerView.adapter = adapter
        if (binding.averageFuelPriceRecyclerView.onFlingListener == null) {
            pagerHelper.attachToRecyclerView(binding.averageFuelPriceRecyclerView)
        }
        binding.indicator.attachToRecyclerView(binding.averageFuelPriceRecyclerView, pagerHelper)
    }

    private fun getCurrentUser() {
        viewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
            HandleResult.handleResult(binding.progressBar, currentUser, onSuccess = { data ->
                viewModel.getUser(data!!.uid)
                getUser()
            })
        }
    }

    private fun getUser() {
        viewModel.userResult.observe(viewLifecycleOwner) { userResult ->
            HandleResult.handleResult(binding.progressBar, userResult, onSuccess = { data ->
                val user = data as User
                binding.initials.text = viewModel.formatInitials(user.name, user.surname)
                binding.userFullName.text = viewModel.formatFullName(user.name, user.surname)
                sharedViewModel.updateData(user)
            })
        }
    }

    private fun checkLastUpdate() {
        if (viewModel.checkLastUpdate()) {
            viewModel.getFuelPrices("istanbul")
            Log.d("TAG", "True")
        } else {
            viewModel.getFuelAverageFuelPrices()
            Log.d("TAG", "False")
        }
    }

    private fun onBackPressCancel() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {}
            }
        )
    }

}