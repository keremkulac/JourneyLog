package com.keremkulac.journeylog.presentation.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.usecase.SignOutUseCase
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {
    private val _signOutResult = MutableLiveData<Result<String>>()
    val signOutResult: LiveData<Result<String>> get() = _signOutResult

    fun signOut() {
        viewModelScope.launch {
            _signOutResult.value = Result.Loading
            signOutUseCase.invoke { result ->
                _signOutResult.value = result
            }
        }
    }
}