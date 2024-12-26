package com.keremkulac.journeylog.presentation.ui.fuelPurchaseAdd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.domain.usecase.GetAllVehiclesUseCase
import com.keremkulac.journeylog.domain.usecase.SaveReceiptUseCase
import com.keremkulac.journeylog.util.InputValidation
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class FuelPurchaseAddViewModel @Inject constructor(
    private val saveReceiptUseCase: SaveReceiptUseCase,
    private val getAllVehiclesUseCase: GetAllVehiclesUseCase,
    private val inputValidation: InputValidation
) : ViewModel() {

    private val _totalPrice = MutableLiveData<String>()
    val totalPrice: LiveData<String> get() = _totalPrice

    private val _totalTax = MutableLiveData<String>()
    val totalTax: LiveData<String> get() = _totalTax

    private val _saveResult = MutableLiveData<Result<String>>()
    val saveResult: LiveData<Result<String>> get() = _saveResult

    private val _validationMessage = MutableLiveData<String>()
    val validationMessage: LiveData<String> get() = _validationMessage

    private val _getAllVehicles = MutableLiveData<Result<Any>>()
    val getAllVehicles: LiveData<Result<Any>> get() = _getAllVehicles


    fun calculateTotal(liter: Double, literPrice: Double) {
        val total = liter * literPrice
        val formattedTotal = String.format("%.2f", total)
        _totalPrice.value = formattedTotal.replace(",",".")
        val formattedTax = String.format("%.2f", calculateTax(total))
        _totalTax.value = formattedTax.replace(",",".")
    }

    private fun calculateTax(total: Double): Double {
        return total * 0.20
    }

    fun getCurrentDate(): List<String> {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val dateTime =
            ZonedDateTime.now(ZoneId.of("Asia/Istanbul")).toLocalDateTime().format(formatter)
        return dateTime.split(" ")
    }

    fun saveReceipt(receipt: Receipt) {
        viewModelScope.launch {
            _saveResult.value = Result.Loading
            saveReceiptUseCase.invoke(receipt) { result ->
                _saveResult.value = result
            }
        }
    }

    fun createUUID(): String {
        return UUID.randomUUID().toString()
    }

    fun getAllVehicles(userId: String) {
        viewModelScope.launch {
            _getAllVehicles.value = Result.Loading
            getAllVehiclesUseCase.invoke(userId) { result ->
                _getAllVehicles.value = result
            }
        }
    }

    fun validateInputs(
        id: String?,
        email: String?,
        stationName: String?,
        fuelType: String?,
        literPrice: String?,
        liter: String?,
        vehicleLicensePlate: String?,
        vehicleLastKm: String?,
        tax: String?,
        total: String?,
        date: String?,
        time: String?
    ): Boolean {
        return inputValidation.validateReceipt(
            id,
            email,
            stationName,
            fuelType,
            literPrice,
            liter,
            vehicleLicensePlate,
            vehicleLastKm,
            tax,
            total,
            date,
            time
        ) { message ->
            _validationMessage.value = message
        }
    }


}