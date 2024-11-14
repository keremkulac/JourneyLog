package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.repository.AuthRepository
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun invoke(token : String,result: (Result<String>) -> Unit) {
        authRepository.signInWithGoogle(token) {
            result.invoke(it)
        }
    }
}