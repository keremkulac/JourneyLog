package com.keremkulac.journeylog.presentation.ui.vehicleCreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.domain.usecase.GetAverageFuelPriceUseCase
import com.keremkulac.journeylog.domain.usecase.SaveVehicleUseCase
import com.keremkulac.journeylog.util.InputValidation
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleCreateViewModel @Inject constructor(
    private val inputValidation: InputValidation,
    private val saveVehicleUseCase: SaveVehicleUseCase,
    private val getAverageFuelPriceUseCase: GetAverageFuelPriceUseCase
) : ViewModel() {

    private val _saveVehicleResult = MutableLiveData<Result<String>>()
    val saveVehicleResult: LiveData<Result<String>> get() = _saveVehicleResult

    private val _validationMessage = MutableLiveData<String>()
    val validationMessage: LiveData<String> get() = _validationMessage

    private val _averageFuelPrices = MutableLiveData<Result<Any>>()
    val averageFuelPrices: LiveData<Result<Any>> get() = _averageFuelPrices

    init {
        getAverageFuelPrice()
    }

    fun validateLicensePlate(selectedVehicleType: String?,licensePlate: String?,lastKm: String?,selectedFuelType : String?,per100KilometerFuel : String?): Boolean {
        return inputValidation.validateLicensePlate(selectedVehicleType,licensePlate,lastKm,selectedFuelType,per100KilometerFuel) { message ->
            _validationMessage.value = message
        }
    }

    fun saveVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            _saveVehicleResult.value = Result.Loading
            saveVehicleUseCase.invoke(vehicle) { result ->
                _saveVehicleResult.value = result
            }
        }
    }

    private fun getAverageFuelPrice() {
        viewModelScope.launch {
            _averageFuelPrices.value = Result.Loading
            getAverageFuelPriceUseCase.invoke {
                _averageFuelPrices.value = it
            }
        }
    }

}