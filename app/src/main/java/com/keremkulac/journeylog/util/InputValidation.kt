package com.keremkulac.journeylog.util

import javax.inject.Inject

class InputValidation @Inject constructor() {

    fun validateEmailAndPassword(
        userEmail: String?,
        userPassword: String?,
        validationMessage: (String) -> Unit
    ): Boolean {
        return when {
            userEmail.isNullOrEmpty() -> {
                validationMessage("Email giriniz")
                false
            }

            userPassword.isNullOrEmpty() -> {
                validationMessage("Şifre giriniz")
                false
            }

            userEmail.isNotEmpty() -> {
                isValidEmail(userEmail) { message ->
                    validationMessage(message)
                }
            }

            else -> true
        }
    }

    fun validateUser(
        userName: String?,
        userLastname: String?,
        userEmail: String?,
        userPassword: String?,
        validationMessage: (String) -> Unit
    ): Boolean {
        return when {
            userName.isNullOrEmpty() -> {
                validationMessage("İsim giriniz")
                false
            }

            userLastname.isNullOrEmpty() -> {
                validationMessage("Soyad giriniz")
                false
            }

            userEmail.isNullOrEmpty() -> {
                validationMessage("Email giriniz")
                false
            }

            userPassword.isNullOrEmpty() -> {
                validationMessage("Şifre giriniz")
                false
            }

            userEmail.isNotEmpty() -> {
                isValidEmail(userEmail) { message ->
                    validationMessage(message)
                }
            }

            else -> true
        }
    }

    private fun isValidEmail(email: String?, validationMessage: (String) -> Unit): Boolean {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email!!).matches()) {
            return true
        } else {
            validationMessage("Geçerli bir email giriniz")
            return false
        }
    }
}