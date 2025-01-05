package com.keremkulac.journeylog.presentation.ui.vehicleView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.domain.usecase.DeleteVehicleUseCase
import com.keremkulac.journeylog.domain.usecase.GetAllVehiclesUseCase
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleViewViewModel @Inject constructor(
    private val getAllVehiclesUseCase: GetAllVehiclesUseCase,
    private val deleteVehicleUseCase: DeleteVehicleUseCase
) : ViewModel() {

    private val _getAllVehicles = MutableLiveData<Result<Any>>()
    val getAllVehicles: LiveData<Result<Any>> get() = _getAllVehicles

    private val _deleteVehicleResult = MutableLiveData<Result<String>>()
    val deleteVehicleResult: LiveData<Result<String>> get() = _deleteVehicleResult

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

    fun deleteVehicle(vehicleId : String){
        viewModelScope.launch {
            _deleteVehicleResult.value = Result.Loading
            deleteVehicleUseCase.invoke(vehicleId){
                _deleteVehicleResult.value = it
            }
        }
    }
}