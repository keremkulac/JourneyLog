package com.keremkulac.journeylog.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.keremkulac.journeylog.domain.usecase.GetCurrentUserUseCase
import com.keremkulac.journeylog.domain.usecase.GetUserUseCase
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) :
    ViewModel() {
    private val _userResult = MutableLiveData<Result<Any>>()
    val userResult : LiveData<Result<Any>> get() = _userResult

    private val _currentUser = MutableLiveData<Result<FirebaseUser?>>()
    val currentUser : LiveData<Result<FirebaseUser?>> get() = _currentUser

    init {
        getCurrentUser()
        getFuelPrices()
    }

    private fun getFuelPrices() {
        viewModelScope.launch {
        }
    }

    private fun getCurrentUser(){
        viewModelScope.launch {
            getCurrentUserUseCase.invoke {
                _currentUser.value = it
            }
        }
    }

    fun getUser(id : String){
        viewModelScope.launch {
            getUserUseCase.invoke(id){
                _userResult.value = it
            }
        }
    }
}