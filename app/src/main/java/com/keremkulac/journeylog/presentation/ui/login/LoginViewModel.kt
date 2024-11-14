package com.keremkulac.journeylog.presentation.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.usecase.KeepUserLoggedInUseCase
import com.keremkulac.journeylog.domain.usecase.LoginUseCase
import com.keremkulac.journeylog.domain.usecase.LoginWithGoogleUseCase
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val keepUserLoggedInUseCase: KeepUserLoggedInUseCase
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> get() = _loginResult

    private val _keepUserLoggedIn = MutableLiveData<Result<String>>()
    val keepUserLoggedIn: LiveData<Result<String>> get() = _keepUserLoggedIn

    private val _validationMessage = MutableLiveData<String>()
    val validationMessage: LiveData<String> get() = _validationMessage

    private val _loginWithGoogle = MutableLiveData<Result<String>>()
    val loginWithGoogle: LiveData<Result<String>> get() = _loginWithGoogle

    init {
        keepUserLoggedIn()
    }

    private fun keepUserLoggedIn() {
        viewModelScope.launch {
            _keepUserLoggedIn.value = Result.Loading
            keepUserLoggedInUseCase.invoke { result ->
                _keepUserLoggedIn.value = result
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = Result.Loading
            loginUseCase.invoke(email, password) { result ->
                _loginResult.value = result
            }
        }
    }

    fun loginWithGoogle(token: String) {
        viewModelScope.launch {
            _loginWithGoogle.value = Result.Loading
            loginWithGoogleUseCase.invoke(token) { result ->
                _loginWithGoogle.value = result
            }

        }
    }

    fun validateInputs(
        userEmail: String?,
        userPassword: String?
    ): Boolean {
        return when {

            userEmail.isNullOrEmpty() -> {
                _validationMessage.value = "Email giriniz"
                false
            }

            userPassword.isNullOrEmpty() -> {
                _validationMessage.value = "Şifre giriniz"
                false
            }

            else -> true
        }
    }

}