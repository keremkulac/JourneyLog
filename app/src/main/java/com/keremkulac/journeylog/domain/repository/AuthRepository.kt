package com.keremkulac.journeylog.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.keremkulac.journeylog.util.Result


interface AuthRepository {

    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        result: (Result<String>) -> Unit
    )

    suspend fun loginUser(
        email: String,
        password: String,
        result: (Result<String>) -> Unit
    )

    suspend fun signOut(result: (Result<String>) -> Unit)

    suspend fun getCurrentUser(result: (Result<FirebaseUser?>) -> Unit)

    suspend fun signInWithGoogle(token: String, result: (Result<Any>) -> Unit)

    suspend fun forgotPassword(email: String, result: (Result<String>) -> Unit)

    suspend fun updatePassword(oldPassword : String,newPassword : String, result: (Result<String>) -> Unit)

}