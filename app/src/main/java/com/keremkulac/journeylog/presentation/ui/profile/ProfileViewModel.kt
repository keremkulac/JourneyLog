package com.keremkulac.journeylog.presentation.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.domain.usecase.GetProfilePictureUrlUseCase
import com.keremkulac.journeylog.domain.usecase.SaveProfilePictureUseCase
import com.keremkulac.journeylog.domain.usecase.SignOutUseCase
import com.keremkulac.journeylog.domain.usecase.UpdateUserUseCase
import com.keremkulac.journeylog.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val saveProfilePictureUseCase: SaveProfilePictureUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getProfilePictureUrlUseCase: GetProfilePictureUrlUseCase
) : ViewModel() {
    private val _signOutResult = MutableLiveData<Result<String>>()
    val signOutResult: LiveData<Result<String>> get() = _signOutResult

    private val _saveProfilePictureResult = MutableLiveData<Result<String>>()
    val saveProfilePictureResult: LiveData<Result<String>> get() = _saveProfilePictureResult

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

    fun saveProfilePicture(imageUri: Uri, path: String) {
        viewModelScope.launch {
            _saveProfilePictureResult.value = Result.Loading
            saveProfilePictureUseCase.invoke(imageUri, path) { result ->
                _saveProfilePictureResult.value = result
            }
        }
    }

    fun getProfilePictureUrl(path: String) {
        viewModelScope.launch {
            _getProfilePictureUrlResult.value = Result.Loading
            getProfilePictureUrlUseCase.invoke(path) { result ->
                _getProfilePictureUrlResult.value = result
            }
        }
    }

    fun createUUID(): String {
        val myUuid = UUID.randomUUID()
        return myUuid.toString()
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            updateUserUseCase.invoke(user) {}
        }
    }

    fun formatFullName(name: String, lastName: String): String {
        val formattedName = name.replaceFirstChar { it.uppercase() }
        val formattedSurname = lastName.replaceFirstChar { it.uppercase() }
        return "$formattedName $formattedSurname"
    }

    fun formatInitials(name: String, lastName: String): String {
        return buildString {
            append(name.firstOrNull()?.uppercase() ?: "")
            append(lastName.firstOrNull()?.uppercase() ?: "")
        }
    }
}