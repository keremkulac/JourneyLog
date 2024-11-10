package com.keremkulac.journeylog.presentation.ui.addFuelPurchase

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.keremkulac.journeylog.databinding.FragmentAddFuelPurchaseBinding
import com.keremkulac.journeylog.util.BaseFragment
import com.keremkulac.journeylog.util.TextWatcher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFuelPurchaseFragment :
    BaseFragment<FragmentAddFuelPurchaseBinding>(FragmentAddFuelPurchaseBinding::inflate) {

    private val viewModel by viewModels<AddFuelPurchaseViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSelectedFuelType()
        observeAndSetValues()
        setDateTime()
    }

    private fun getSelectedFuelType() {
        binding.receiptPurchasedFuelType.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedType = group.findViewById<Chip>(checkedIds[0]).text.toString()
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
                }else{
                    binding.receiptTotalPrice.text = ""
                    binding.receiptTotalTax.text = ""
                }
            }
        }
        liter.addTextChangedListener(textWatcher)
        literPrice.addTextChangedListener(textWatcher)
    }

    private fun setDateTime(){
        val dateTime = viewModel.getCurrentDate()
        binding.receiptDate.text = dateTime[0]
        binding.receiptTime.text = dateTime[1]
    }
}