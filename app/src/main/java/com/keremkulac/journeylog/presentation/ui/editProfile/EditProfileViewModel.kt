package com.keremkulac.journeylog.presentation.ui.editProfile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.domain.usecase.GetProfilePictureUrlUseCase
import com.keremkulac.journeylog.domain.usecase.SaveProfilePictureUseCase
import com.keremkulac.journeylog.domain.usecase.UpdateUserUseCase
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getProfilePictureUrlUseCase: GetProfilePictureUrlUseCase,
    private val saveProfilePictureUseCase: SaveProfilePictureUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {

    private val _getProfilePictureUrlResult = MutableLiveData<Result<String>>()
    val getProfilePictureUrlResult: LiveData<Result<String>> get() = _getProfilePictureUrlResult

    private val _saveProfilePictureResult = MutableLiveData<Result<String>>()
    val saveProfilePictureResult: LiveData<Result<String>> get() = _saveProfilePictureResult

    private val _updateUserResult = MutableLiveData<Result<String>>()
    val updateUserResult: LiveData<Result<String>> get() = _updateUserResult

    private val _validationMessage = MutableLiveData<String>()
    val validationMessage: LiveData<String> get() = _validationMessage

    fun getProfilePictureUrl(path: String) {
        viewModelScope.launch {
            _getProfilePictureUrlResult.value = Result.Loading
            getProfilePictureUrlUseCase.invoke(path) { result ->
                _getProfilePictureUrlResult.value = result
            }
        }
    }

    fun saveProfilePicture(imageUri: Uri, path: String) {
        viewModelScope.launch {
            _saveProfilePictureResult.value = Result.Loading
            saveProfilePictureUseCase.invoke(imageUri, path) { result ->
                _saveProfilePictureResult.value = result
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            _updateUserResult.value = Result.Loading
            updateUserUseCase.invoke(user) {
                _updateUserResult.value = it
            }
        }
    }

    fun validateInputs(
        userName: String?,
        userLastname: String?,
        userEmail: String?
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