package com.keremkulac.journeylog.presentation.ui.fuelPurchaseView


import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentFuelPurchaseViewBinding
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.FuelType
import com.keremkulac.journeylog.util.HandleResult
import com.keremkulac.journeylog.util.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FuelPurchaseViewFragment :
    BaseFragment<FragmentFuelPurchaseViewBinding>(FragmentFuelPurchaseViewBinding::inflate) {

    private val viewModel by viewModels<FuelPurchaseViewViewModel>()
    private lateinit var sharedViewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        observeUser()
        navigateAddFuelPurchase()
        observeAllReceipts()
    }

    private fun observeUser() {
        sharedViewModel.sharedData.observe(viewLifecycleOwner) { user ->
            viewModel.getAllReceipts(user.email)
        }
    }

    private fun navigateAddFuelPurchase() {
        binding.fab.setOnClickListener {
            findNavController().navigate(
                R.id.action_fuelPurchaseViewFragment_to_FuelPurchaseAddFragment
            )
        }
    }

    private fun setPieChart(receiptList: List<Receipt>) {
        val pieEntries = createPieEntries(receiptList)
        val pieDataSet = createPieDataSet(pieEntries)
        val pieData = createPieData(pieDataSet)

        setupPieChart(pieData)
    }

    private fun getChartColors(): List<Int> = listOf(
        Color.parseColor("#FF6361"),
        Color.parseColor("#58508D"),
        Color.parseColor("#FFA600")
    )

    private fun createPieDataSet(entries: ArrayList<PieEntry>): PieDataSet {
        return PieDataSet(entries, "").apply {
            valueTextSize = 12f
            colors = getChartColors()
            valueTextColor = Color.BLACK
        }
    }

    private fun createPieData(dataSet: PieDataSet): PieData {
        return PieData(dataSet).apply {
            setDrawValues(true)
        }
    }

    private fun setupPieChart(pieData: PieData) {
        binding.pieChart.apply {
            description.isEnabled = false
            setDrawEntryLabels(false)
            data = pieData
            invalidate()
        }
    }

    private fun createPieEntries(receiptList: List<Receipt>): ArrayList<PieEntry> {
        return ArrayList<PieEntry>().apply {
            FuelType.entries.forEach { fuelType ->
                add(viewModel.calculateFuelTypePrice(receiptList, fuelType.value))
            }
        }
    }

    private fun observeAllReceipts() {
        viewModel.allReceipts.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result,
                onSuccess = { data ->
                    val list = data as List<Receipt>
                    createRecyclerView(list)
                    binding.totalPrice.text =
                        getString(R.string.fuel_purchase_view_total_price).format(
                            viewModel.calculateTotalPrice(list)
                        )
                    setPieChart(list)
                })
        }
    }

    private fun createRecyclerView(list: List<Receipt>) {
        val adapter = FuelPurchaseViewAdapter()
        binding.fuelPurchaseRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.receiptList = list as ArrayList<Receipt>
        binding.fuelPurchaseRecyclerView.adapter = adapter
        clickListener(adapter)
        checkFuelPurchaseList(list)
    }

    private fun clickListener(adapter: FuelPurchaseViewAdapter) {
        adapter.clickListener = { receipt ->
            findNavController().navigate(
                FuelPurchaseViewFragmentDirections.actionFuelPurchaseViewFragmentToFuelPurchaseDetailFragment(
                    receipt
                )
            )
        }
    }

    private fun checkFuelPurchaseList(list: List<Receipt>) {
        if (list.isEmpty()) {
            binding.fuelPurchaseRecyclerView.visibility = View.GONE
            binding.emptyWarning.visibility = View.VISIBLE
        } else {
            binding.fuelPurchaseRecyclerView.visibility = View.VISIBLE
            binding.emptyWarning.visibility = View.GONE
        }
    }
}