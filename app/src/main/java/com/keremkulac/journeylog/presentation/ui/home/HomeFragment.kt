package com.keremkulac.journeylog.presentation.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
import com.keremkulac.journeylog.util.TranslationHelper
import com.keremkulac.journeylog.util.toMoneyFormat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var barDataSet: BarDataSet
    private var toggle: Boolean = false
    private var selectedFuelType: String? = null
    private lateinit var averageFuelPriceList: List<AverageFuelPrice>

    @Inject
    lateinit var translationHelper: TranslationHelper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        onBackPressCancel()
        getCurrentUser()
        observeAverageFuelPrices()
        observeAllReceipts()
        lastFuelPurchaseCardViewToggle()
        fuelConsumption()
    }

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
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_fuel_consumption)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val window = dialog.window
            val params = WindowManager.LayoutParams()
            if (window != null) {
                params.apply {
                    copyFrom(window.attributes)
                    width = WindowManager.LayoutParams.MATCH_PARENT
                    height = WindowManager.LayoutParams.WRAP_CONTENT
                }
                window.attributes = params
            }

            val fuelType = dialog.findViewById<AutoCompleteTextView>(R.id.fuelType)
            val calculate = dialog.findViewById<Button>(R.id.calculateButton)
            val liter = dialog.findViewById<EditText>(R.id.kmLiter)
            val per100Kilometer = dialog.findViewById<TextView>(R.id.per100Kilometer)
            val perKilometer = dialog.findViewById<TextView>(R.id.perKilometer)
            val view1 = dialog.findViewById<View>(R.id.view1)
            val view2 = dialog.findViewById<View>(R.id.view2)
            selectFuelType(fuelType)
            createFuelType(fuelType)
            calculate.setOnClickListener {
                averageFuelPriceList.forEach { averageFuelPrice ->
                    if (averageFuelPrice.title == selectedFuelType) {
                        val perKilometerInfo = liter.text.toString().toDouble() * averageFuelPrice.value.toDouble()
                        val per100KilometerInfo = perKilometerInfo / 100
                        per100Kilometer.visibility = View.VISIBLE
                        perKilometer.visibility = View.VISIBLE
                        view1.visibility = View.VISIBLE
                        view2.visibility = View.VISIBLE
                        perKilometer.text = getString(R.string.fuel_consumption_per_kilometer_info).format(selectedFuelType,perKilometerInfo.toMoneyFormat())
                        per100Kilometer.text = getString(R.string.fuel_consumption_per_100_kilometers_info).format(selectedFuelType,per100KilometerInfo.toMoneyFormat())
                    }
                }
            }
            dialog.show()
        }
    }

    private fun createFuelType(fuelType: AutoCompleteTextView) {
        val adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.item_dropdown,
                resources.getStringArray(R.array.fuelTypes)
            )
        fuelType.setAdapter(adapter)
    }

    private fun selectFuelType(fuelType: AutoCompleteTextView) {
        fuelType.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                selectedFuelType = parent.getItemAtPosition(position).toString()
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

    private fun lastFuelPurchaseCardViewToggle() {
        binding.lastFuelPurchaseCardViewToggle.setOnClickListener {
            if (!toggle) {
                binding.lastFuelPurchaseCardView.visibility = View.VISIBLE
                binding.lastFuelPurchaseCardViewToggle.background =
                    requireContext().getDrawable(R.drawable.toggle_style_active)
                binding.openFuelPurchaseCardView.setImageDrawable(requireContext().getDrawable(R.drawable.ic_close))
                toggle = true
            } else {
                binding.lastFuelPurchaseCardView.visibility = View.GONE
                binding.lastFuelPurchaseCardViewToggle.background =
                    requireContext().getDrawable(R.drawable.toogle_style_deactive)
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