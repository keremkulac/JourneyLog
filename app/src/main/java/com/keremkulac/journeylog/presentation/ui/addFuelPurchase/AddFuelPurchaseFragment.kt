package com.keremkulac.journeylog.presentation.ui.addFuelPurchase

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.keremkulac.journeylog.databinding.FragmentAddFuelPurchaseBinding
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.TextWatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class AddFuelPurchaseFragment :
    BaseFragment<FragmentAddFuelPurchaseBinding>(FragmentAddFuelPurchaseBinding::inflate) {

    private val viewModel by viewModels<AddFuelPurchaseViewModel>()
    private var selectedType = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAndSetValues()
        getSelectedFuelType()
        setDateTime()
        saveReceipt()
    }

    private fun getSelectedFuelType() {
        binding.receiptPurchasedFuelType.setOnCheckedStateChangeListener { group, checkedIds ->
            selectedType = group.findViewById<Chip>(checkedIds[0]).text.toString()
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
        binding.receiptDate.text = dateTime[0]
        binding.receiptTime.text = dateTime[1]
    }

    private fun saveReceipt() {

        binding.receiptSave.setOnClickListener {
            val id = viewModel.createUUID()
            val email = viewModel.currentUser()?.email
            val stationName = binding.receiptStation.text.trim().toString()
            val fuelType = selectedType
            val literPrice = binding.receiptLiterPrice.text.trim().toString()
            val liter = binding.receiptPurchaseLiter.text.trim().toString()
            val tax = binding.receiptTotalTax.text.trim().toString()
            val total = binding.receiptTotalPrice.text.trim().toString()
            val date = binding.receiptDate.text.trim().toString()
            val time = binding.receiptTime.text.trim().toString()
            Log.d("TAG", id.toString())
            Log.d("TAG", email.toString())
            Log.d("TAG", stationName.toString())
            Log.d("TAG", fuelType.toString())
            Log.d("TAG", literPrice.toString())
            Log.d("TAG", liter.toString())
            Log.d("TAG", tax.toString())
            Log.d("TAG", total.toString())
            Log.d("TAG", date.toString())
            Log.d("TAG", time.toString())

            if (id.isBlank() || email.isNullOrBlank() || stationName.isBlank() || fuelType.isBlank() || literPrice.isBlank() || liter.isBlank() ||
                tax.isBlank() || total.isBlank() || date.isBlank() || time.isBlank()
            ) {
                Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val receipt = Receipt(
                    id = id,
                    email = email,
                    stationName = stationName,
                    fuelType = fuelType,
                    literPrice = literPrice,
                    liter = liter,
                    tax = tax,
                    total = total,
                    date = date,
                    time = time
                )
               viewModel.saveReceipt(receipt)
            }
        }
    }

}