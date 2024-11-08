package com.keremkulac.journeylog.domain.repository

import com.google.firebase.auth.FirebaseAuth
import com.keremkulac.journeylog.util.AuthResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository{
    override suspend fun registerUser(email: String, password: String): AuthResult {
        return try {
            val data = auth.createUserWithEmailAndPassword(email, password).await()
            AuthResult.Success(data)
        } catch (e: Exception) {
            AuthResult.Error(e.message)
        }
    }

    override suspend fun loginUser(email: String, password: String): AuthResult {
        return try {
           val data =  auth.signInWithEmailAndPassword(email,password).await()
            AuthResult.Success(data)
        }catch (e : Exception){
            AuthResult.Error(e.message)
        }
    }
}