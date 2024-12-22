package com.keremkulac.journeylog.presentation.ui.fuelPurchaseView


import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class FuelPurchaseViewFragment :
    BaseFragment<FragmentFuelPurchaseViewBinding>(FragmentFuelPurchaseViewBinding::inflate) {

    private val viewModel by viewModels<FuelPurchaseViewViewModel>()
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var searchView: SearchView
    private lateinit var adapter: FuelPurchaseViewAdapter

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
                    checkFuelPurchaseList(list)
                    binding.totalPrice.text =
                        getString(R.string.fuel_purchase_view_total_price).format(
                            viewModel.calculateTotalPrice(list)
                        )
                    setPieChart(list)
                })
        }
    }

    private fun createRecyclerView(list: List<Receipt>) {
        adapter = FuelPurchaseViewAdapter()
        binding.fuelPurchaseRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter.receiptList = list as ArrayList<Receipt>
        binding.fuelPurchaseRecyclerView.adapter = adapter
        clickListener(adapter)
        optionsMenu(list)
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
            adapter.filterList(list as ArrayList<Receipt>)
        }
    }

    private fun optionsMenu(receiptList: List<Receipt>) {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_search, menu)
                val search = menu.findItem(R.id.action_search)
                searchView = search.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (!newText.isNullOrEmpty()) {
                            checkFuelPurchaseList(viewModel.filter(newText, receiptList))
                        }
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_search -> {
                        showDatePicker()
                        true
                    }

                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val selectedDate = formatDate(day, month, year)
                searchView.setQuery(selectedDate, false)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        val minDate = Calendar.getInstance()
        minDate.add(Calendar.MONTH, -6)
        datePickerDialog.datePicker.minDate = minDate.timeInMillis
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    private fun formatDate(day: Int, month: Int, year: Int): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return dateFormat.format(calendar.time)
    }

}