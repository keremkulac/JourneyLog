package com.keremkulac.journeylog.domain.usecase

import com.google.firebase.auth.FirebaseUser
import com.keremkulac.journeylog.domain.repository.AuthRepositoryImp
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(private val authRepositoryImp: AuthRepositoryImp) {
    suspend fun invoke(
        result: (Result<FirebaseUser?>) -> Unit
    ) {
        authRepositoryImp.getCurrentUser {
            result(it)
        }
    }
}