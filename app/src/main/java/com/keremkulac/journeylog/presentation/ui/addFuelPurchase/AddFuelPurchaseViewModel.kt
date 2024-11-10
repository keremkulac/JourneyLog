package com.keremkulac.journeylog.presentation.ui.addFuelPurchase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddFuelPurchaseViewModel @Inject constructor(): ViewModel() {

    private val _totalPrice = MutableLiveData<String>()
    val totalPrice: LiveData<String> get() = _totalPrice

    private val _totalTax = MutableLiveData<String>()
    val totalTax: LiveData<String> get() = _totalTax

    fun calculateTotal(liter: Double, literPrice: Double) {
        val total = liter * literPrice
        val formattedTotal = String.format("%.2f",total)
        _totalPrice.value = formattedTotal
        val formattedTax = String.format("%.2f",calculateTax(total))
        _totalTax.value = formattedTax
    }

    private fun calculateTax(total: Double): Double {
        return total * 0.20
    }

    fun getCurrentDate(): List<String> {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val dateTime = ZonedDateTime.now(ZoneId.of("Asia/Istanbul")).toLocalDateTime().format(formatter)
        return dateTime.split(" ")
    }
}