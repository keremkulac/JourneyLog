package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.repository.AuthRepositoryImp
import javax.inject.Inject

class IsUserLoggedInUseCase @Inject constructor(private val authRepositoryImp: AuthRepositoryImp) {
    suspend fun invoke() = authRepositoryImp.isUserLoggedIn()
}