package com.keremkulac.journeylog.presentation.ui.fuelPurchaseAdd

import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.keremkulac.journeylog.R
import com.keremkulac.journeylog.databinding.FragmentFuelPurchaseAddBinding
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.CustomDialog
import com.keremkulac.journeylog.util.HandleResult
import com.keremkulac.journeylog.util.SharedViewModel
import com.keremkulac.journeylog.util.SuccessfulDialogUtil
import com.keremkulac.journeylog.util.TextWatcher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FuelPurchaseAddFragment :
    BaseFragment<FragmentFuelPurchaseAddBinding>(FragmentFuelPurchaseAddBinding::inflate) {

    private val viewModel by viewModels<FuelPurchaseAddViewModel>()
    private lateinit var sharedViewModel: SharedViewModel
    private var selectedType = ""
    private var selectedCompany = ""
    private var selectedLicensePlate = ""
    private var companyList = listOf<String>()
    private var licensePlateList = listOf<String>()
    private var dateTime = listOf<String>()
    private var user: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        observeSaveResult()
        observeCurrentUser()
        observeAllVehicles()
        observeAndSetValues()
        getSelectedFuelType()
        setDateTime()
        saveReceipt()
        createCompaniesAdapter()
        selectCompany()
        selectLicensePlate()
        observeValidation()
        createVehicle()

    }

    private fun getSelectedFuelType() {
        binding.receiptPurchasedFuelType.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                selectedType = group.findViewById<Chip>(checkedIds[0]).text.toString()
            }
        }
    }

    private fun observeAndSetValues() {
        viewModel.totalPrice.observe(viewLifecycleOwner) { total ->
            binding.receiptTotalPrice.text = total.toString()
        }

        viewModel.totalTax.observe(viewLifecycleOwner) { tax ->
            binding.receiptTotalTax.text = tax.toString()
        }
        setTextWatcher()
    }

    private fun setTextWatcher() {
        val liter = binding.receiptPurchaseLiter
        val literPrice = binding.receiptLiterPrice
        val textWatcher = object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                super.onTextChanged(p0, p1, p2, p3)
                val literValue = liter.text.toString().toDoubleOrNull()
                val literPriceValue = literPrice.text.toString().toDoubleOrNull()
                if (literValue != null && literPriceValue != null) {
                    viewModel.calculateTotal(literValue, literPriceValue)
                } else {
                    binding.receiptTotalPrice.text = ""
                    binding.receiptTotalTax.text = ""
                }
            }
        }
        liter.addTextChangedListener(textWatcher)
        literPrice.addTextChangedListener(textWatcher)
    }

    private fun setDateTime() {
        val dateTime = viewModel.getCurrentDate()
        this.dateTime = dateTime
    }

    private fun saveReceipt() {
        binding.receiptSave.setOnClickListener {
            if (isValid()) {
                createDialog(getReceipt())
            }
        }
    }

    private fun observeCurrentUser() {
        sharedViewModel.sharedData.observe(viewLifecycleOwner) { user ->
            viewModel.getAllVehicles(user.id)
            this.user = user
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun observeAllVehicles() {
        viewModel.getAllVehicles.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result,
                onSuccess = { data ->
                    licensePlateList = createLicensePlateList(data as List<Vehicle>)
                    val adapter =
                        ArrayAdapter(requireContext(), R.layout.item_dropdown, licensePlateList)
                    binding.licensePlate.setAdapter(adapter)
                },
                onFailure = {})
        }
    }

    private fun createLicensePlateList(list: List<Vehicle>): List<String> {
        checkList(list)
        return list.map { it.licensePlate!! }
    }

    private fun checkList(list: List<Vehicle>) {
        if (list.isEmpty()) {
            binding.createVehicle.visibility = View.VISIBLE
            binding.vehicleLicensePlateTitle.visibility = View.GONE
            binding.licensePlateLayout.visibility = View.GONE
            binding.vehicleKmTitle.visibility = View.GONE
            binding.vehicleKmLayout.visibility = View.GONE

        } else {
            binding.createVehicle.visibility = View.GONE
            binding.vehicleLicensePlateTitle.visibility = View.VISIBLE
            binding.licensePlateLayout.visibility = View.VISIBLE
            binding.vehicleKmTitle.visibility = View.VISIBLE
            binding.vehicleKmLayout.visibility = View.VISIBLE
        }
    }

    private fun observeSaveResult() {
        viewModel.saveResult.observe(viewLifecycleOwner) { result ->
            HandleResult.handleResult(binding.progressBar, result,
                onSuccess = {
                    SuccessfulDialogUtil(
                        requireContext(),
                        getString(R.string.dialog_success_fuel_purchase_message)
                    ).showDialog()
                    findNavController().navigate(R.id.action_fuelPurchaseAddFragment_to_fuelPurchaseViewFragment)
                },
                onFailure = { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun createCompaniesAdapter() {
        requireContext().apply {
            companyList = resources.getStringArray(R.array.companies).toList()
            val adapter = ArrayAdapter(this, R.layout.item_dropdown, companyList)
            binding.receiptStation.setAdapter(adapter)
        }
    }

    private fun observeValidation() {
        viewModel.validationMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectLicensePlate() {
        binding.licensePlate.onItemClickListener =
            OnItemClickListener { parent, _, position, _ ->
                selectedLicensePlate = parent.getItemAtPosition(position).toString()
            }
    }

    private fun selectCompany() {
        binding.receiptStation.onItemClickListener =
            OnItemClickListener { parent, _, position, _ ->
                selectedCompany = parent.getItemAtPosition(position).toString()
            }
    }

    private fun createDialog(receipt: Receipt) {
        requireContext().apply {
            CustomDialog.showConfirmationDialog(
                this,
                getString(R.string.dialog_add_fuel_purchase_title),
                getString(R.string.dialog_add_fuel_purchase_message),
                getString(R.string.dialog_add_fuel_purchase_positive_button_text),
                getString(R.string.dialog_add_fuel_purchase_negative_button_text),
                onPositiveClick = {
                    viewModel.saveReceipt(receipt)
                }
            )
        }
    }

    private fun getReceipt(): Receipt {
        return Receipt(
            id = viewModel.createUUID(),
            email = user?.email.toString().trim(),
            stationName = selectedCompany.trim(),
            fuelType = selectedType.trim(),
            literPrice = binding.receiptLiterPrice.text.toString().trim(),
            liter = binding.receiptPurchaseLiter.text.toString().trim(),
            vehicleLicensePlate = selectedLicensePlate.trim(),
            vehicleLastKm = binding.vehicleKm.text.toString().trim(),
            tax = binding.receiptTotalTax.text.toString().trim(),
            total = binding.receiptTotalPrice.text.toString().trim(),
            date = dateTime[0],
            time = dateTime[1]
        )
    }

    private fun isValid(): Boolean {
        val isValid = viewModel.validateInputs(
            viewModel.createUUID(),
            user?.email.toString().trim(),
            selectedCompany.trim(),
            selectedType.trim(),
            binding.receiptLiterPrice.text.toString().trim(),
            binding.receiptPurchaseLiter.text.toString().trim(),
            selectedLicensePlate.trim(),
            binding.vehicleKm.text.toString().trim(),
            binding.receiptTotalTax.text.toString().trim(),
            binding.receiptTotalPrice.text.toString().trim(),
            date = dateTime[0],
            time = dateTime[1]
        )
        return isValid
    }

    private fun createVehicle() {
        binding.createVehicle.setOnClickListener {
            findNavController().navigate(
                FuelPurchaseAddFragmentDirections.actionFuelPurchaseAddFragmentToVehicleCreateFragment(
                    true
                )
            )
        }
    }

}