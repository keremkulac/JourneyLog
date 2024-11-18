package com.keremkulac.journeylog.presentation.ui.updatePassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.usecase.UpdatePasswordUseCase
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdatePasswordViewModel @Inject constructor(
    private val updatePasswordUseCase: UpdatePasswordUseCase
) : ViewModel() {
    private val _updatePasswordResult = MutableLiveData<Result<String>>()
    val updatePasswordResult: LiveData<Result<String>> = _updatePasswordResult

    private val _validationMessage = MutableLiveData<String>()
    val validationMessage: LiveData<String> get() = _validationMessage

    fun updatePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _updatePasswordResult.value = Result.Loading
            updatePasswordUseCase.invoke(oldPassword, newPassword) {
                _updatePasswordResult.value = it
            }
        }
    }

    fun validatePassword(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Boolean {
        return when {
            oldPassword.isEmpty() -> {
                _validationMessage.value = "Eski şifre boş bırakılamaz."
                false
            }

            newPassword.isEmpty() -> {
                _validationMessage.value = "Yeni şifre boş bırakılamaz."
                false
            }

            confirmPassword.isEmpty() -> {
                _validationMessage.value = "Yeni şifre onay boş bırakılamaz."
                false
            }

            oldPassword.length < 6 -> {
                _validationMessage.value = "Eski şifre en az 6 karakter olmalıdır."
                false
            }

            newPassword.length < 6 -> {
                _validationMessage.value = "Yeni şifre en az 6 karakter olmalıdır."
                false
            }

            confirmPassword.length < 6 -> {
                _validationMessage.value = "Yeni şifre tekrarı en az 6 karakter olmalıdır."
                false
            }

            newPassword != confirmPassword -> {
                _validationMessage.value = "Şifreler eşleşmiyor."
                false
            }

            else -> true
        }

    }

}