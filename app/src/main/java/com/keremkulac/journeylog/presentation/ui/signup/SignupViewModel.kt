package com.keremkulac.journeylog.presentation.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.domain.usecase.RegisterUseCase
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> get() = _registerResult

    private val _validationMessage = MutableLiveData<String>()
    val validationMessage: LiveData<String> get() = _validationMessage

    fun register(email: String, password: String, user: User) {
        viewModelScope.launch {
            _registerResult.value = Result.Loading
            registerUseCase.invoke(email, password, user) { result ->
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

}