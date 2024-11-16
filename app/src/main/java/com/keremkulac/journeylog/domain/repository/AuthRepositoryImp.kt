package com.keremkulac.journeylog.domain.repository

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    private val auth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient,
    private val context: Context
) : AuthRepository {

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        result: (Result<String>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                result.invoke(Result.Success(task.result.user?.uid ?: ""))
            } else {
                try {
                    throw task.exception ?: java.lang.Exception("Invalid authentication")
                } catch (e: FirebaseAuthWeakPasswordException) {
                    result.invoke(Result.Failure("Şifre en az 6 karakterden oluşmalı"))
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    result.invoke(Result.Failure("Geçersiz email girildi. Lütfen kontrol edip tekrar deneyin"))
                } catch (e: FirebaseAuthUserCollisionException) {
                    result.invoke(Result.Failure("Bu email zaten kayıtlı"))
                } catch (e: Exception) {
                    result.invoke(Result.Failure(e.message))
                }
            }
        }
    }


    override suspend fun loginUser(
        email: String,
        password: String,
        result: (Result<String>) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                result.invoke(Result.Success("Giriş başarılı"))
            } else {
                try {
                    throw task.exception ?: java.lang.Exception("Invalid authentication")
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    result.invoke(Result.Failure("Geçersiz giriş bilgileri"))
                } catch (e: FirebaseAuthInvalidUserException) {
                    result.invoke(Result.Failure("Kullanıcı bulunamadı"))
                } catch (e: Exception) {
                    result.invoke(Result.Failure("Bilinmeyen bir hata oluştu"))
                }
            }
        }
    }

    override suspend fun keepUserLoggedIn(
        result: (Result<String>) -> Unit
    ) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            result.invoke(Result.Success("Kullanıcı daha önce giriş yapmış"))
        } else {
            result.invoke(Result.Failure("Kullanıcı daha önce giriş yapmamış"))
        }
    }

    override suspend fun signOut(result: (Result<String>) -> Unit) {
        auth.signOut()
        Result.Success("Çıkış yapıldı")
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
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.user?.let { firebaseUser ->
                    val googleAccount = GoogleSignIn.getLastSignedInAccount(context)
                    val user = User(
                        id = firebaseUser.uid,
                        name = googleAccount?.givenName ?: "",
                        surname = googleAccount?.familyName ?: "",
                        email = googleAccount?.email ?: ""
                    )
                    result.invoke(Result.Success(user))
                }
            } else {
                result.invoke(
                    (Result.Failure(task.exception?.message ?: "Firebase sign-in failed"))
                )
            }
        }
    }

    override suspend fun forgotPassword(email: String, result: (Result<String>) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                result.invoke(Result.Success("Şifre sıfırlama maili gönderildi"))
            } else {
                result.invoke(Result.Failure("Şifre sıfırlama maili gönderilemedi"))
            }
        }
    }

}