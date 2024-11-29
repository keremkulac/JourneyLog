package com.keremkulac.journeylog.presentation.ui.fuelPurchase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.usecase.GetAllReceiptsUseCase
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FuelPurchaseViewModel @Inject constructor(
    private val getAllReceiptsUseCase: GetAllReceiptsUseCase
) : ViewModel() {
    private val _allReceipts = MutableLiveData<Result<Any>>()
    val allReceipts: LiveData<Result<Any>> get() = _allReceipts

    init {
        getAllReceipts()
    }

    private fun getAllReceipts() {
        viewModelScope.launch {
            _allReceipts.value = Result.Loading
            getAllReceiptsUseCase.invoke {
                _allReceipts.value = it
            }
        }
    }
}