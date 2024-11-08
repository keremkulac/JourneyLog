package com.keremkulac.journeylog.util

sealed class AuthResult {
    data class Success(val data: Any?): AuthResult()
    data class Error(val error: String?): AuthResult()
}