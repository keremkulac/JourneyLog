package com.keremkulac.journeylog.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.keremkulac.journeylog.domain.usecase.GetCurrentUserUseCase
import com.keremkulac.journeylog.domain.usecase.GetFuelOilPricesUseCase
import com.keremkulac.journeylog.domain.usecase.GetUserUseCase
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getFuelOilPricesUseCase: GetFuelOilPricesUseCase
) :
    ViewModel() {
    private val _userResult = MutableLiveData<Result<Any>>()
    val userResult: LiveData<Result<Any>> get() = _userResult

    private val _currentUser = MutableLiveData<Result<FirebaseUser?>>()
    val currentUser: LiveData<Result<FirebaseUser?>> get() = _currentUser

    private val _fuelPrices = MutableLiveData<Result<Any>>()
    val fuelPrices: LiveData<Result<Any>> get() = _fuelPrices

    init {
        getCurrentUser()
    }

    fun getFuelPrices(city : String) {
        viewModelScope.launch {
            getFuelOilPricesUseCase.invoke(city) {
                _fuelPrices.value = it
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