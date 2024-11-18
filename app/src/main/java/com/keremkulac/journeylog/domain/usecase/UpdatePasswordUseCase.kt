package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.repository.AuthRepository
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend fun invoke(oldPassword: String,newPassword: String, result: (Result<String>) -> Unit) {
        authRepository.updatePassword(oldPassword,newPassword) {
            result.invoke(it)
        }
    }
}