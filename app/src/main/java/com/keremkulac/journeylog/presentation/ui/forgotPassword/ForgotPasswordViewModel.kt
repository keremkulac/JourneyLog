package com.keremkulac.journeylog.presentation.ui.forgotPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.usecase.ForgotPasswordUseCase
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {
    private val _forgotPasswordResult = MutableLiveData<Result<String>>()
    val forgotPasswordResult: LiveData<Result<String>> get() = _forgotPasswordResult

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _forgotPasswordResult.value = Result.Loading
            forgotPasswordUseCase.invoke(email) { result ->
                _forgotPasswordResult.value = result
            }
        }
    }
}