package com.keremkulac.journeylog.presentation.ui.vehicleSelect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.util.InputValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VehicleSelectViewModel @Inject constructor(
    private val inputValidation: InputValidation
) : ViewModel() {

    private val _validationMessage = MutableLiveData<String>()
    val validationMessage: LiveData<String> get() = _validationMessage

    fun validateLicensePlate(licensePlate: String?,vehicle: Vehicle?): Boolean {
        return inputValidation.validateLicensePlate(licensePlate,vehicle) { message ->
            _validationMessage.value = message
        }
    }

}