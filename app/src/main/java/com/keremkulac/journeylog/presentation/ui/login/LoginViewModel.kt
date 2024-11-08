package com.keremkulac.journeylog.presentation.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.usecase.LoginUseCase
import com.keremkulac.journeylog.util.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
)  : ViewModel() {
    private val _loginResult = MutableLiveData<AuthResult>()
    val loginResult : LiveData<AuthResult> get() = _loginResult

    fun login(email : String, password : String){
        viewModelScope.launch {
            _loginResult.value = loginUseCase.invoke(email,password)
        }
    }
}