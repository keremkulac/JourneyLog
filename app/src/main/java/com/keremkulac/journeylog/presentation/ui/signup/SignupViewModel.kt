package com.keremkulac.journeylog.presentation.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.usecase.RegisterUseCase
import com.keremkulac.journeylog.util.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
): ViewModel() {
    private val _registerResult = MutableLiveData<AuthResult>()
    val registerResult : LiveData<AuthResult> get() = _registerResult


    fun register(email : String, password : String){
        viewModelScope.launch {
            _registerResult.value = registerUseCase.invoke(email,password)
        }
    }

}