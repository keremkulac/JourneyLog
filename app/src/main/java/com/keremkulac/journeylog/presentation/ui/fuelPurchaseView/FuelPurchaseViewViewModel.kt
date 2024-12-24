package com.keremkulac.journeylog.presentation.ui.fuelPurchaseView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.PieEntry
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.domain.usecase.GetAllReceiptsUseCase
import com.keremkulac.journeylog.util.Result
import com.keremkulac.journeylog.util.TranslationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FuelPurchaseViewViewModel @Inject constructor(
    private val getAllReceiptsUseCase: GetAllReceiptsUseCase
) : ViewModel() {
    private val _allReceipts = MutableLiveData<Result<Any>>()
    val allReceipts: LiveData<Result<Any>> get() = _allReceipts


    fun getAllReceipts(email: String) {
        viewModelScope.launch {
            _allReceipts.value = Result.Loading
            getAllReceiptsUseCase.invoke(email) {
                _allReceipts.value = it
            }
        }
    }

    fun calculateTotalPrice(list: List<Receipt>): Double {
        val total = list.sumOf { it.total.replace(",", ".").toDouble() }
        return String.format(Locale.US, "%.2f", total).toDouble()
    }

    fun calculateFuelTypePrice(list: List<Receipt>, fuelType: String): PieEntry {
        var total = 0.0
        for (item in list) {
            if (TranslationHelper.translateManually(item.fuelType) == fuelType) {
                total += item.total.replace(",", ".").toDouble()
            }
        }
        return PieEntry(total.toFloat(), fuelType)
    }

    fun filter(text: String,list: List<Receipt>) : ArrayList<Receipt>{
        val filteredList: ArrayList<Receipt> = ArrayList()
        for (item in list) {
            if (item.date.lowercase().contains(text.lowercase())) {
                filteredList.add(item)
            }
        }
        return filteredList
    }
}