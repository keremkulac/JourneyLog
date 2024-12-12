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
import com.keremkulac.journeylog.util.HandleResult
import com.keremkulac.journeylog.util.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var barDataSet: BarDataSet
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        onBackPressCancel()
        getCurrentUser()
        observeAverageFuelPrices()
        observeAllReceipts()

    }


    private fun observeAverageFuelPrices() {
        viewModel.averageFuelPrices.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result, onSuccess = { data ->
                val averageFuelPriceList = data as List<AverageFuelPrice>

                setRecyclerView(ArrayList(averageFuelPriceList))
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
                viewModel.getAllReceipts(user.email)
            })
        }
    }

    private fun observeAllReceipts() {
        viewModel.allReceipts.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result, onSuccess = { data ->
                val list = data as List<Receipt>
                setupBarChart(viewModel.getReceiptDates(list))
                loadBarChartData(viewModel.getBarDataSet(list))
            })
        }
    }

    private fun setupBarChart(dates: List<String>) {
        with(binding.barChart) {
            description.isEnabled = false
            setDragEnabled(true)
            setVisibleXRangeMaximum(3f)
            axisLeft.apply {
                setDrawGridLines(false)
                axisLineWidth = 2f
                textSize = 10f
            }
            axisRight.apply {
                setDrawGridLines(false)
                isEnabled = false
            }
            xAxis.apply {
                textSize = 10f
                axisLineWidth = 2f
                position = XAxis.XAxisPosition.BOTTOM
                setCenterAxisLabels(true)
                granularity = 1f
                isGranularityEnabled = true
                setDrawGridLines(false)
                valueFormatter = IndexAxisValueFormatter(dates)
                //  axisMinimum = 0.50f
            }

            xAxis.setDrawAxisLine(true)
            xAxis.setDrawGridLinesBehindData(true)

            animateY(2000)
        }
    }

    private fun loadBarChartData(barEntryList : List<BarEntry>) {
        barDataSet = BarDataSet(barEntryList, "").apply {
            color = requireContext().getColor(R.color.main)
        }
        barDataSet.valueTextSize = 16f

        val data = BarData(barDataSet).apply {
            barWidth = 0.15f
        }

        binding.barChart.data = data
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