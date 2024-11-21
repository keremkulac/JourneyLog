package com.keremkulac.journeylog.presentation.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.domain.usecase.GetCurrentUserUseCase
import com.keremkulac.journeylog.domain.usecase.LoginUseCase
import com.keremkulac.journeylog.domain.usecase.LoginWithGoogleUseCase
import com.keremkulac.journeylog.domain.usecase.RegisterUseCase
import com.keremkulac.journeylog.util.InputValidation
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val registerUseCase: RegisterUseCase,
    private val inputValidation: InputValidation
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> get() = _loginResult

    private val _currentUser = MutableLiveData<Result<FirebaseUser?>>()
    val currentUser: LiveData<Result<FirebaseUser?>> get() = _currentUser

    private val _validationMessage = MutableLiveData<String>()
    val validationMessage: LiveData<String> get() = _validationMessage

    private val _loginWithGoogleResult = MutableLiveData<Result<Any>>()
    val loginWithGoogleResult: LiveData<Result<Any>> get() = _loginWithGoogleResult

    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> get() = _registerResult

    init {
        keepUserLoggedIn()
    }

    private fun keepUserLoggedIn() {
        viewModelScope.launch {
            _currentUser.value = Result.Loading
            getCurrentUserUseCase.invoke { result ->
                _currentUser.value = result
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
            _loginWithGoogleResult.value = Result.Loading
            loginWithGoogleUseCase.invoke(token) { result ->
                _loginWithGoogleResult.value = result
            }

        }
    }

    fun register(user: User) {
        viewModelScope.launch {
            _registerResult.value = Result.Loading
            registerUseCase.invoke(user) { result ->
                _registerResult.value = result
            }
        }
    }

    fun validateInputs(
        userEmail: String?,
        userPassword: String?
    ): Boolean {
        return inputValidation.validateEmailAndPassword(userEmail, userPassword) { message ->
            _validationMessage.value = message
        }

    }


}