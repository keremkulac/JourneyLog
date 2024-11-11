package com.keremkulac.journeylog.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.util.AuthResult


interface AuthRepository {
    suspend fun registerUser(email: String, password: String): AuthResult

    suspend fun loginUser(email: String,password: String) : AuthResult

    suspend fun signOut() : AuthResult

    suspend fun isUserLoggedIn() : AuthResult

    suspend fun saveReceipt(receipt: Receipt) : AuthResult

    suspend fun getCurrentUser() : FirebaseUser?
}