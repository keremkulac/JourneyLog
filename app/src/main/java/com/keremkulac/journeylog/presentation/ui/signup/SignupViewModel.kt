package com.keremkulac.journeylog.presentation.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.domain.usecase.CreateUserWithEmailAndPasswordUseCase
import com.keremkulac.journeylog.domain.usecase.RegisterUseCase
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val createUserWithEmailAndPasswordUseCase: CreateUserWithEmailAndPasswordUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> get() = _registerResult

    private val _validationMessage = MutableLiveData<String>()
    val validationMessage: LiveData<String> get() = _validationMessage

    private val _createUserWithEmailAndPassword = MutableLiveData<Result<String>>()
    val createUserWithEmailAndPassword: LiveData<Result<String>> get() = _createUserWithEmailAndPassword

    fun createUserWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _createUserWithEmailAndPassword.value = Result.Loading
            createUserWithEmailAndPasswordUseCase.invoke(email, password) { result ->
                _createUserWithEmailAndPassword.value = result
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
        userName: String?,
        userLastname: String?,
        userEmail: String?,
        userPassword: String?
    ): Boolean {
        return when {
            userName.isNullOrEmpty() -> {
                _validationMessage.value = "İsim giriniz"
                false
            }

            userLastname.isNullOrEmpty() -> {
                _validationMessage.value = "Soyad giriniz"
                false
            }

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

    fun isValidEmail(email: String): Boolean {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true
        } else {
            _validationMessage.value = "Geçerli bir email giriniz"
            return false
        }
    }

}