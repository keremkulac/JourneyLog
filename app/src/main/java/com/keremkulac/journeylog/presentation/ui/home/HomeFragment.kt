package com.keremkulac.journeylog.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.databinding.FragmentHomeBinding
import com.keremkulac.journeylog.domain.model.AverageFuelPrice
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.util.ExpandableLayoutManager
import com.keremkulac.journeylog.util.FuelConsumptionDialogUtil
import com.keremkulac.journeylog.util.HandleResult
import com.keremkulac.journeylog.util.SharedViewModel
import com.keremkulac.journeylog.util.TranslationHelper
import com.keremkulac.journeylog.util.TripFuelCostCalculationDialogUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var barDataSet: BarDataSet
    private lateinit var averageFuelPriceList: List<AverageFuelPrice>
    private lateinit var vehicleList: List<Vehicle>
    private lateinit var expandableLayoutManager: ExpandableLayoutManager

    @Inject
    lateinit var translationHelper: TranslationHelper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        onBackPressCancel()
        getCurrentUser()
        observeAverageFuelPrices()
        observeAllReceipts()
        observeAllVehicles()
        lastFuelPurchaseCardViewToggle()
        fuelConsumption()
        tripFuelCosCalculation()
    }

    @Suppress("UNCHECKED_CAST")
    private fun observeAverageFuelPrices() {
        viewModel.averageFuelPrices.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result, onSuccess = { data ->
                val averageFuelPriceList = data as List<AverageFuelPrice>
                this.averageFuelPriceList = averageFuelPriceList
                setRecyclerView(ArrayList(averageFuelPriceList))
            })
        }
    }

    private fun setRecyclerView(averageFuelPriceList: ArrayList<AverageFuelPrice>) {
        val adapter = AverageFuelPriceAdapter(translationHelper)
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

    private fun fuelConsumption() {
        binding.fuelConsumptionCardView.setOnClickListener {
            FuelConsumptionDialogUtil(requireContext(), averageFuelPriceList).showDialog()
        }
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
                viewModel.getAllReceipts(user.email)
                viewModel.getAllVehicles(user.id)
            })
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun observeAllReceipts() {
        viewModel.allReceipts.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result,
                onSuccess = { data ->
                    val list = data as List<Receipt>
                    loadBarChartData(viewModel.getBarDataSet(list), viewModel.getReceiptDates(list))
                })
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun observeAllVehicles() {
        viewModel.allVehicles.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result,
                onSuccess = { data ->
                    val list = data as List<Vehicle>
                    vehicleList = list
                })
        }
    }

    private fun loadBarChartData(barEntryList: List<BarEntry>, dates: List<String>) {
        barDataSet = BarDataSet(barEntryList, "").apply {
            color = requireContext().getColor(R.color.main)
        }

        val barData = BarData(barDataSet).apply {
            barWidth = 0.15f
        }

        barDataSet.valueTextSize = 14f
        binding.barChart.apply {
            legend.isEnabled = false
            data = barData
            animateY(1000)
            setFitBars(true)
            description.isEnabled = false
            setDragEnabled(true)
            axisLeft.apply {
                setDrawGridLines(false)
                axisLineWidth = 2f
                textSize = 12f
            }

            axisRight.apply {
                setDrawGridLines(false)
                isEnabled = false
            }
            xAxis.apply {
                textSize = 12f
                granularity = 1f
                setDrawGridLines(false)
                axisLineWidth = 2f
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = IndexAxisValueFormatter(dates)
            }

        }
    }

    private fun lastFuelPurchaseCardViewToggle() {
        binding.apply {
            expandableLayoutManager = ExpandableLayoutManager(this.openFuelPurchaseCardView)
            lastFuelPurchaseCardView.setOnClickListener { expandableLayoutManager.toggleLayout(this.lastFuelPurchaseLayout) }
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

    private fun tripFuelCosCalculation() {
        binding.tripFuelCostCalculation.setOnClickListener {
            TripFuelCostCalculationDialogUtil(requireContext(), averageFuelPriceList,vehicleList).showDialog()
        }

    }


}