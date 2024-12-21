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
    private var toggle : Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        onBackPressCancel()
        getCurrentUser()
        observeAverageFuelPrices()
        observeAllReceipts()
        lastFuelPurchaseCardViewToggle()
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
            HandleResult.handleResult(binding.progressBar, result,
                onSuccess = { data ->
                    val list = data as List<Receipt>
                    loadBarChartData(viewModel.getBarDataSet(list), viewModel.getReceiptDates(list))
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

    private fun lastFuelPurchaseCardViewToggle(){
        binding.lastFuelPurchaseCardViewToggle.setOnClickListener {
            if (!toggle){
                binding.lastFuelPurchaseCardView.visibility = View.VISIBLE
                binding.lastFuelPurchaseCardViewToggle.background = requireContext().getDrawable(R.drawable.toggle_style_active)
                binding.openFuelPurchaseCardView.setImageDrawable(requireContext().getDrawable(R.drawable.ic_close))
                toggle = true
            }else{
                binding.lastFuelPurchaseCardView.visibility = View.GONE
                binding.lastFuelPurchaseCardViewToggle.background = requireContext().getDrawable(R.drawable.toogle_style_deactive)
                binding.openFuelPurchaseCardView.setImageDrawable(requireContext().getDrawable(R.drawable.ic_open))
                toggle = false
            }
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