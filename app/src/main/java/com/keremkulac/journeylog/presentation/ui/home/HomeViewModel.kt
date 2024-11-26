package com.keremkulac.journeylog.presentation.ui.home


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.keremkulac.journeylog.data.local.model.AverageFuelPriceEntity
import com.keremkulac.journeylog.domain.model.DistrictData
import com.keremkulac.journeylog.domain.usecase.GetCurrentUserUseCase
import com.keremkulac.journeylog.domain.usecase.GetFromRoomAverageFuelUseCase
import com.keremkulac.journeylog.domain.usecase.GetFuelOilPricesUseCase
import com.keremkulac.journeylog.domain.usecase.GetUserUseCase
import com.keremkulac.journeylog.domain.usecase.SaveFromRoomAverageFuelUseCase
import com.keremkulac.journeylog.util.FuelPriceOperations
import com.keremkulac.journeylog.util.FuelType
import com.keremkulac.journeylog.util.LastUpdateSharedPreferences
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getFuelOilPricesUseCase: GetFuelOilPricesUseCase,
    private val fuelPriceOperations: FuelPriceOperations,
    private val saveFromRoomAverageFuelUseCase: SaveFromRoomAverageFuelUseCase,
    private val getFromRoomAverageFuelUseCase: GetFromRoomAverageFuelUseCase,
    private val lastUpdateSharedPreferences: LastUpdateSharedPreferences
) :
    ViewModel() {
    private val _userResult = MutableLiveData<Result<Any>>()
    val userResult: LiveData<Result<Any>> get() = _userResult

    private val _currentUser = MutableLiveData<Result<FirebaseUser?>>()
    val currentUser: LiveData<Result<FirebaseUser?>> get() = _currentUser

    private val _fuelPrices = MutableLiveData<Result<Any>>()
    val fuelPrices: LiveData<Result<Any>> get() = _fuelPrices

    private val _averageFuelPrices = MutableLiveData<Result<HashMap<String, Double>>>()
    val averageFuelPrices: LiveData<Result<HashMap<String, Double>>> get() = _averageFuelPrices

    init {
        getCurrentUser()
        checkFirstStart()
    }

    fun getFuelPrices(city: String) {
        Log.d("TAG1", "API")
        viewModelScope.launch {
            _fuelPrices.value = Result.Loading
            getFuelOilPricesUseCase.invoke(city) {
                _fuelPrices.value = it
            }
        }
    }

    fun getFuelAverageFuelPrices() {
        viewModelScope.launch {
            Log.d("TAG1", "ROOM")
            val list = getFromRoomAverageFuelUseCase.invoke()
            val hashMap = HashMap<String, Double>().apply {
                list.forEach {
                    put(it.title, it.price)
                }
            }
            Log.d("TAG1", hashMap.toString())
            _averageFuelPrices.value = Result.Loading
            _averageFuelPrices.value = Result.Success(hashMap)
        }

    }

    fun calculateAveragePrice(fuelPriceList: List<DistrictData>) {
        val averageFuelPricesHashMap = HashMap<String, Double>()
        averageFuelPricesHashMap["Gasoline"] =
            fuelPriceOperations.calculateAveragePrice(fuelPriceList, FuelType.GASOLINE)
        averageFuelPricesHashMap["LPG"] =
            fuelPriceOperations.calculateAveragePrice(fuelPriceList, FuelType.LPG)
        averageFuelPricesHashMap["Diesel"] =
            fuelPriceOperations.calculateAveragePrice(fuelPriceList, FuelType.DIESEL)
        _averageFuelPrices.value = Result.Loading
        _averageFuelPrices.value = Result.Success(averageFuelPricesHashMap)
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

    fun saveAverageFuelPrices(averageFuelPriceList: List<AverageFuelPriceEntity>) {
        viewModelScope.launch {
            saveFromRoomAverageFuelUseCase.invoke(averageFuelPriceList)
        }
    }

    private fun getSharedPreferencesTime(): String? {
        return lastUpdateSharedPreferences.getData()
    }

    fun saveSharedPreferencesTime(time: Long) {
        return lastUpdateSharedPreferences.saveData(time)
    }

    fun checkLastUpdate(): Boolean {
        val time = getSharedPreferencesTime()
        return if (time != null) {
            System.currentTimeMillis() - time.toLong() > 3_600_000
        } else
            false
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

    private fun checkFirstStart() {
        if (lastUpdateSharedPreferences.getData() == null) {
            getFuelPrices("istanbul")
        }
    }
}