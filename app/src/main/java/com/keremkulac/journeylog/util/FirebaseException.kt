package com.keremkulac.journeylog.util

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthMultiFactorException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseAuthWebException
import java.lang.Exception
import javax.inject.Inject

class FirebaseException @Inject constructor() {
    fun findExceptionMessage(exception: Exception): String {
        return when (exception) {
            is FirebaseAuthInvalidUserException -> "Kullanıcı bulunamadı"
            is FirebaseAuthWeakPasswordException -> "Şifre en az 6 haneli olmalı"
            is FirebaseAuthEmailException -> "Email işlemi başarısız"
            is FirebaseAuthUserCollisionException -> "Bu email adresi zaten kullanımda"
            is FirebaseTooManyRequestsException -> "Çok fazla deneme yapıldı"
            is FirebaseNetworkException -> "İnternet bağlantısı hatası"
            is FirebaseAuthRecentLoginRequiredException -> "Yeniden giriş yapmanız gerekiyor"
            is FirebaseAuthMultiFactorException -> "İki faktörlü doğrulama gerekli"
            is FirebaseAuthActionCodeException -> "Doğrulama kodu geçersiz"
            is FirebaseAuthWebException -> "Web authentication hatası"
            is FirebaseAuthInvalidCredentialsException -> "Geçersiz kimlik bilgileri"
            is FirebaseAuthException -> "Kimlik doğrulama hatası"
            else -> "Beklenmeyen bir hata oluştu"
        }
    }
}