package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.repository.AuthRepositoryImp
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class CreateUserWithEmailAndPasswordUseCase @Inject constructor(private val authRepositoryImp: AuthRepositoryImp) {
    suspend operator fun invoke(
        email: String,
        password: String,
        result: (Result<String>) -> Unit
    ) {
        authRepositoryImp.createUserWithEmailAndPassword(email, password) {
            result(it)
        }
    }
}