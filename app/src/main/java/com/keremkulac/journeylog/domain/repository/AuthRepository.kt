package com.keremkulac.journeylog.domain.repository

import com.keremkulac.journeylog.util.AuthResult


interface AuthRepository {
    suspend fun registerUser(email: String, password: String): AuthResult

    suspend fun loginUser(email: String,password: String) : AuthResult
}