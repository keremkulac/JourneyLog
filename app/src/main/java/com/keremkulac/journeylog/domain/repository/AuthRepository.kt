package com.keremkulac.journeylog.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.keremkulac.journeylog.util.Result


interface AuthRepository {

    suspend fun registerUser(email: String, password: String, result: (Result<String>) -> Unit)

    suspend fun loginUser(email: String, password: String, result: (Result<String>) -> Unit)

    suspend fun keepUserLoggedIn(result: (Result<String>) -> Unit)

    suspend fun signOut(result: (Result<String>) -> Unit)

    suspend fun getCurrentUser(result: (Result<FirebaseUser?>) -> Unit)

}