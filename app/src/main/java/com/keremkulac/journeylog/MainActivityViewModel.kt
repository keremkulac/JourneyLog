package com.keremkulac.journeylog

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(): ViewModel() {

    private val _bottomNavigationVisibility = MutableLiveData<Int>()
    val bottomNavVisibility: LiveData<Int> = _bottomNavigationVisibility

    fun bottomNavigationVisibility(destinationId: Int) {
        _bottomNavigationVisibility.value = when (destinationId) {
            R.id.loginFragment, R.id.signupFragment -> View.GONE
            else -> View.VISIBLE
        }
    }

}