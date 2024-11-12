package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.repository.AuthRepositoryImp
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class KeepUserLoggedInUseCase @Inject constructor(private val authRepositoryImp: AuthRepositoryImp) {
    suspend fun invoke(
        result: (Result<String>) -> Unit
    ) {
        authRepositoryImp.keepUserLoggedIn {
            result(it)
        }
    }
}