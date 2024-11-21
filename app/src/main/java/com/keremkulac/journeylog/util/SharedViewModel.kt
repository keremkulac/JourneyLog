package com.keremkulac.journeylog.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keremkulac.journeylog.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(): ViewModel(){

    private val _sharedData = MutableLiveData<User>()
    val sharedData: LiveData<User> get() = _sharedData

    fun updateData(data: User) {
        _sharedData.value = data
    }

}