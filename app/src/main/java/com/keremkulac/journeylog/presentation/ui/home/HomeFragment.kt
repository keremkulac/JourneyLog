package com.keremkulac.journeylog.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.databinding.FragmentHomeBinding
import com.keremkulac.journeylog.domain.model.AverageFuelPrice
import com.keremkulac.journeylog.domain.model.DistrictData
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.util.FuelPriceOperations
import com.keremkulac.journeylog.util.FuelTypeTranslation
import com.keremkulac.journeylog.util.Result
import com.keremkulac.journeylog.util.SharedViewModel
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
        getCurrentUser()
        binding.circleIcon.setOnClickListener {
            viewModel.getFuelPrices("istanbul")
            observeFuelPrices()
        }
        observeAverageFuelPrices()
    }

    private fun observeFuelPrices() {
        viewModel.fuelPrices.observe(viewLifecycleOwner) {
            when (it) {
                Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Failure -> {
                    binding.progressBar.visibility = View.GONE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    viewModel.calculateAveragePrice(it.data as List<DistrictData>)
                }
            }
        }
    }

    private fun observeAverageFuelPrices() {
        viewModel.averageFuelPrices.observe(viewLifecycleOwner) { averageFuelPrices ->
            val averageFuelPriceList = ArrayList<AverageFuelPrice>()
            for (averagePrice in averageFuelPrices) {
                val averageFuelPrice = AverageFuelPrice(
                    FuelTypeTranslation.translateKey(averagePrice.key),
                    averagePrice.value
                )
                averageFuelPriceList.add(averageFuelPrice)
            }
            setRecyclerView(averageFuelPriceList)
        }
    }

    private fun setRecyclerView(averageFuelPriceList: ArrayList<AverageFuelPrice>) {
        binding.averageFuelPriceRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        PagerSnapHelper().attachToRecyclerView(binding.averageFuelPriceRecyclerView)
        val adapter = AverageFuelPriceAdapter()
        adapter.averageFuelPriceList = averageFuelPriceList
        binding.averageFuelPriceRecyclerView.adapter = adapter
    }

    private fun getCurrentUser() {
        viewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
            when (currentUser) {
                Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Failure -> binding.progressBar.visibility = View.GONE
                is Result.Success -> {
                    viewModel.getUser(currentUser.data!!.uid)
                    getUser()
                }
            }
        }
    }

    private fun getUser() {
        viewModel.userResult.observe(viewLifecycleOwner) { userResult ->
            when (userResult) {
                Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Failure -> binding.progressBar.visibility = View.GONE
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val user = userResult.data as User
                    binding.initials.text = viewModel.formatInitials(user.name, user.surname)
                    binding.userFullName.text = viewModel.formatFullName(user.name, user.surname)
                    sharedViewModel.updateData(user)
                }
            }
        }
    }

    private fun onBackPressCancel() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                }
            })
    }

}