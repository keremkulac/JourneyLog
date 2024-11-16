package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.repository.AuthRepository
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend fun invoke(email : String,result: (Result<String>) -> Unit){
        authRepository.forgotPassword(email){
            result.invoke(it)
        }
    }
}