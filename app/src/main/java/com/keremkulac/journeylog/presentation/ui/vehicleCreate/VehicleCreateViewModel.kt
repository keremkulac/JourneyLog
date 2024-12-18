package com.keremkulac.journeylog.presentation.ui.vehicleCreate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.domain.usecase.SaveVehicleUseCase
import com.keremkulac.journeylog.util.InputValidation
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleCreateViewModel @Inject constructor(
    private val inputValidation: InputValidation,
    private val saveVehicleUseCase: SaveVehicleUseCase
) : ViewModel() {

    private val _saveVehicleResult = MutableLiveData<Result<String>>()
    val saveVehicleResult: LiveData<Result<String>> get() = _saveVehicleResult

    private val _validationMessage = MutableLiveData<String>()
    val validationMessage: LiveData<String> get() = _validationMessage

    fun validateLicensePlate(vehicle: Vehicle?,licensePlate: String?,lastKm: String?): Boolean {
        return inputValidation.validateLicensePlate(vehicle,licensePlate,lastKm) { message ->
            _validationMessage.value = message
        }
    }

    fun saveVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            saveVehicleUseCase.invoke(vehicle) { result ->
                _saveVehicleResult.value = result
            }
        }
    }

}