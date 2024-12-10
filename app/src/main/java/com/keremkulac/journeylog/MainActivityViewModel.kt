package com.keremkulac.journeylog

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {

    private val _bottomNavigationVisibility = MutableLiveData<Int>()
    val bottomNavVisibility: LiveData<Int> = _bottomNavigationVisibility

    private val _toolbarVisibility = MutableLiveData<Int>()
    val toolbarVisibility: LiveData<Int> = _toolbarVisibility

    fun bottomNavigationVisibility(destinationId: Int) {
        _bottomNavigationVisibility.value = when (destinationId) {
            R.id.loginFragment,
            R.id.signupFragment,
            R.id.forgotPasswordFragment -> View.GONE

            else -> View.VISIBLE
        }
    }

    fun toolBarVisibility(destinationId : Int){
        _toolbarVisibility.value = when (destinationId) {
            R.id.loginFragment,
            R.id.signupFragment,
            R.id.forgotPasswordFragment,
            R.id.homeFragment-> View.GONE

            else -> View.VISIBLE
        }
    }

}