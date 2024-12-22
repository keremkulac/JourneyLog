package com.keremkulac.journeylog.presentation.ui.vehicleView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.domain.usecase.GetAllVehiclesUseCase
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleViewViewModel @Inject constructor(
    private val getAllVehiclesUseCase: GetAllVehiclesUseCase
) : ViewModel() {

    private val _getAllVehicles = MutableLiveData<Result<Any>>()
    val getAllVehicles: LiveData<Result<Any>> get() = _getAllVehicles

    fun getAllVehicles(userId: String) {
        viewModelScope.launch {
            _getAllVehicles.value = Result.Loading
            getAllVehiclesUseCase.invoke(userId) { result ->
                _getAllVehicles.value = result
            }
        }
    }

    fun filter(text: String,list: List<Vehicle>) : ArrayList<Vehicle>{
        val filteredList: ArrayList<Vehicle> = ArrayList()
        for (item in list) {
            if (item.licensePlate!!.lowercase().contains(text.lowercase())) {
                filteredList.add(item)
            }
        }
        return filteredList
    }
}