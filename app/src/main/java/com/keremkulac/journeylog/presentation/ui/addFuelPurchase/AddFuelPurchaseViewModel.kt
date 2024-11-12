package com.keremkulac.journeylog.presentation.ui.addFuelPurchase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.domain.usecase.GetCurrentUserUseCase
import com.keremkulac.journeylog.domain.usecase.SaveReceiptUseCase
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddFuelPurchaseViewModel @Inject constructor(
    private val saveReceiptUseCase: SaveReceiptUseCase,
    private val getCurrentUser: GetCurrentUserUseCase
) : ViewModel() {

    private val _totalPrice = MutableLiveData<String>()
    val totalPrice: LiveData<String> get() = _totalPrice

    private val _totalTax = MutableLiveData<String>()
    val totalTax: LiveData<String> get() = _totalTax

    private val _saveResult = MutableLiveData<Result<String>>()
    val saveResult: LiveData<Result<String>> get() = _saveResult

    fun calculateTotal(liter: Double, literPrice: Double) {
        val total = liter * literPrice
        val formattedTotal = String.format("%.2f", total)
        _totalPrice.value = formattedTotal
        val formattedTax = String.format("%.2f", calculateTax(total))
        _totalTax.value = formattedTax
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

    fun currentUser(): FirebaseUser? {
        var firebaseUser: FirebaseUser? = null
        viewModelScope.launch {
            getCurrentUser.invoke { _result ->

                when (_result) {
                    Result.Loading -> TODO()
                    is Result.Failure -> TODO()
                    is Result.Success -> {
                        firebaseUser = _result.data
                    }
                }
            }
        }
        return firebaseUser
    }


}