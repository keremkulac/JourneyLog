package com.keremkulac.journeylog.presentation.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.usecase.GetProfilePictureUrlUseCase
import com.keremkulac.journeylog.domain.usecase.SignOutUseCase
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val getProfilePictureUrlUseCase: GetProfilePictureUrlUseCase
) : ViewModel() {
    private val _signOutResult = MutableLiveData<Result<String>>()
    val signOutResult: LiveData<Result<String>> get() = _signOutResult

    private val _getProfilePictureUrlResult = MutableLiveData<Result<String>>()
    val getProfilePictureUrlResult: LiveData<Result<String>> get() = _getProfilePictureUrlResult

    fun signOut() {
        viewModelScope.launch {
            _signOutResult.value = Result.Loading
            signOutUseCase.invoke { result ->
                _signOutResult.value = result
            }
        }
    }


    fun getProfilePictureUrl(path: String) {
        viewModelScope.launch {
            Log.d("TAG","getProfilePictureUrl")
            _getProfilePictureUrlResult.value = Result.Loading
            getProfilePictureUrlUseCase.invoke(path) { result ->
                _getProfilePictureUrlResult.value = result
            }
        }
    }

    fun formatFullName(name: String, lastName: String): String {
        val formattedName = name.replaceFirstChar { it.uppercase() }
        val formattedSurname = lastName.replaceFirstChar { it.uppercase() }
        return "$formattedName $formattedSurname"
    }

}