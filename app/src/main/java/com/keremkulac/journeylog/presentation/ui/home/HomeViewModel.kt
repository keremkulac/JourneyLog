package com.keremkulac.journeylog.presentation.ui.home


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.auth.FirebaseUser
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.domain.usecase.GetAllReceiptsUseCase
import com.keremkulac.journeylog.domain.usecase.GetAllVehiclesUseCase
import com.keremkulac.journeylog.domain.usecase.GetAverageFuelPriceUseCase
import com.keremkulac.journeylog.domain.usecase.GetCurrentUserUseCase
import com.keremkulac.journeylog.domain.usecase.GetUserUseCase
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getAverageFuelPriceUseCase: GetAverageFuelPriceUseCase,
    private val getAllReceiptsUseCase: GetAllReceiptsUseCase,
    private val getAllVehiclesUseCase: GetAllVehiclesUseCase
) :
    ViewModel() {
    private val _userResult = MutableLiveData<Result<Any>>()
    val userResult: LiveData<Result<Any>> get() = _userResult

    private val _currentUser = MutableLiveData<Result<FirebaseUser?>>()
    val currentUser: LiveData<Result<FirebaseUser?>> get() = _currentUser

    private val _averageFuelPrices = MutableLiveData<Result<Any>>()
    val averageFuelPrices: LiveData<Result<Any>> get() = _averageFuelPrices

    private val _allReceipts = MutableLiveData<Result<Any>>()
    val allReceipts: LiveData<Result<Any>> get() = _allReceipts

    private val _allVehicles = MutableLiveData<Result<Any>>()
    val allVehicles: LiveData<Result<Any>> get() = _allVehicles

    init {
        getCurrentUser()
        getAverageFuelPrice()
    }

    private fun getAverageFuelPrice() {
        viewModelScope.launch {
            _averageFuelPrices.value = Result.Loading
            getAverageFuelPriceUseCase.invoke {
                _averageFuelPrices.value = it
            }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = Result.Loading
            getCurrentUserUseCase.invoke {
                _currentUser.value = it
            }
        }
    }

    fun getUser(id: String) {
        viewModelScope.launch {
            _userResult.value = Result.Loading
            getUserUseCase.invoke(id) {
                _userResult.value = it
            }
        }
    }

    fun getAllReceipts(email: String) {
        viewModelScope.launch {
            _allReceipts.value = Result.Loading
            getAllReceiptsUseCase.invoke(email) {
                _allReceipts.value = it
            }
        }
    }

    fun getAllVehicles(id: String) {
        viewModelScope.launch {
            _allVehicles.value = Result.Loading
            getAllVehiclesUseCase.invoke(id) {
                _allVehicles.value = it
            }
        }
    }

    fun getReceiptDates(receiptList: List<Receipt>): List<String> {
        var count = 0
        val days = mutableListOf<String>()
        for (receipt in receiptList) {
            if (count < 3) {
                days.add(receipt.date)
                count++
            }
        }
        return days
    }

    fun getBarDataSet(receiptList: List<Receipt>): List<BarEntry> {
        var count = 0
        val barEntryList = mutableListOf<BarEntry>()
        for (receipt in receiptList) {
            if (count < 3) {
                barEntryList.add(BarEntry(count.toFloat(), receipt.liter.toFloat()))
                count++
            }
        }
        return barEntryList
    }

    fun formatFullName(name: String, lastName: String): String {
        val formattedName = name.replaceFirstChar { it.uppercase() }
        val formattedSurname = lastName.replaceFirstChar { it.uppercase() }
        return "$formattedName $formattedSurname"
    }

    fun formatInitials(name: String, lastName: String): String {
        return buildString {
            append(name.firstOrNull()?.uppercase() ?: "")
            append(lastName.firstOrNull()?.uppercase() ?: "")
        }
    }

}