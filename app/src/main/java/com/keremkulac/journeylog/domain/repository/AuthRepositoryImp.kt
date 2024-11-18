package com.keremkulac.journeylog.domain.repository

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.util.FirebaseException
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    private val auth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient,
    private val context: Context,
    private val firebaseException: FirebaseException
) : AuthRepository {

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        result: (Result<String>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { authResult ->
            result.invoke(Result.Success(authResult.user?.uid ?: ""))
        }.addOnFailureListener { exception ->
            result.invoke(Result.Failure(firebaseException.findExceptionMessage(exception)))
        }
    }


    override suspend fun loginUser(
        email: String,
        password: String,
        result: (Result<String>) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                result.invoke(Result.Success("Giriş başarılı"))
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(firebaseException.findExceptionMessage(exception)))
            }
    }


    override suspend fun signOut(result: (Result<String>) -> Unit) {
        try {
            auth.signOut()
            result.invoke(Result.Success("Çıkış yapıldı"))
        } catch (e: Exception) {
            result.invoke(Result.Failure("Beklenmeyen hata"))
        }
    }

    override suspend fun getCurrentUser(result: (Result<FirebaseUser?>) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            result.invoke(Result.Success(currentUser))
        } else {
            result.invoke(Result.Failure("Kullanıcı bulunamadı"))
        }
    }


    override suspend fun signInWithGoogle(token: String, result: (Result<Any>) -> Unit) {
        val signInIntent = googleSignInClient.signInIntent
        GoogleSignIn.getSignedInAccountFromIntent(signInIntent)
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                authResult.user?.let { firebaseUser ->
                    val googleAccount = GoogleSignIn.getLastSignedInAccount(context)
                    val user = User(
                        id = firebaseUser.uid,
                        name = googleAccount?.givenName ?: "",
                        surname = googleAccount?.familyName ?: "",
                        email = googleAccount?.email ?: ""
                    )
                    result.invoke(Result.Success(user))
                }
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(firebaseException.findExceptionMessage(exception)))
            }
    }

    override suspend fun forgotPassword(email: String, result: (Result<String>) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { task ->
                result.invoke(Result.Success("Şifre sıfırlama maili gönderildi"))
            }.addOnFailureListener { exception ->
                result.invoke(Result.Failure(firebaseException.findExceptionMessage(exception)))
            }
    }

}