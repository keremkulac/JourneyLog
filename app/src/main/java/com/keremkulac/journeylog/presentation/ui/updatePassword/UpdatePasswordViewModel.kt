package com.keremkulac.journeylog.presentation.ui.updatePassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.usecase.UpdatePasswordUseCase
import com.keremkulac.journeylog.util.InputValidation
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdatePasswordViewModel @Inject constructor(
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val inputValidation: InputValidation
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
        return inputValidation.validateUpdatePassword(
            oldPassword,
            newPassword,
            confirmPassword
        ) { message ->
            _validationMessage.value = message
        }
    }

}