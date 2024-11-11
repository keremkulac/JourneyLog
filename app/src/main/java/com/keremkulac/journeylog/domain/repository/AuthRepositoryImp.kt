package com.keremkulac.journeylog.domain.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.keremkulac.journeylog.domain.model.Receipt
import com.keremkulac.journeylog.util.AuthResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
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

    override suspend fun signOut(): AuthResult {
        return try {
            val data = auth.signOut()
            AuthResult.Success(data)
        }catch (e : Exception){
            AuthResult.Error(e.message)
        }
    }

    override suspend fun isUserLoggedIn(): AuthResult {
        return try {
            val data = auth.currentUser
            AuthResult.Success(data)
        }catch (e : Exception){
            AuthResult.Error(e.message)
        }
    }

    override suspend fun saveReceipt(receipt: Receipt): AuthResult {
        return try {
           val data = firestore.collection("receipt").add(receipt)
               .addOnSuccessListener {}
               .addOnFailureListener { e->
                   AuthResult.Error(e.message)
               }
            AuthResult.Success(data)
        }catch (e : Exception){
            AuthResult.Error(e.message)
        }
    }

    override suspend fun getCurrentUser() : FirebaseUser? {
        return try {
            auth.currentUser
        }catch (e : Exception){
            null
        }
    }

}