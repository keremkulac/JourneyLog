package com.keremkulac.journeylog.presentation.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.usecase.IsUserLoggedInUseCase
import com.keremkulac.journeylog.domain.usecase.LoginUseCase
import com.keremkulac.journeylog.util.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase
)  : ViewModel() {
    private val _loginResult = MutableLiveData<AuthResult>()
    val loginResult : LiveData<AuthResult> get() = _loginResult

    private val _isLoggedUserIn = MutableLiveData<AuthResult>()
    val isLoggedUserIn : LiveData<AuthResult> get() = _isLoggedUserIn

    init {
        isLoggedUserIn()
    }


    fun login(email : String, password : String){
        viewModelScope.launch {
            _loginResult.value = loginUseCase.invoke(email,password)
        }
    }

    private fun isLoggedUserIn(){
        viewModelScope.launch {
            _isLoggedUserIn.value = isUserLoggedInUseCase.invoke()
        }
    }
}