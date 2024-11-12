package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.domain.repository.AuthRepositoryImp
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val authRepositoryImp: AuthRepositoryImp) {
    suspend operator fun invoke(
        email: String,
        password: String,
        user: User,
        result: (Result<String>) -> Unit
    ) {
        authRepositoryImp.registerUser(email, password, user) {
            result(it)
        }
    }
}