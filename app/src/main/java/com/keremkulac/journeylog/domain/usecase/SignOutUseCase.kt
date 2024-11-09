package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.repository.AuthRepositoryImp
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val authRepositoryImp: AuthRepositoryImp) {
    suspend fun invoke() = authRepositoryImp.signOut()
}