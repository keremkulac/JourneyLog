package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.repository.AuthRepositoryImp
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val authRepositoryImp: AuthRepositoryImp) {
    suspend fun invoke(email : String, password : String) = authRepositoryImp.registerUser(email,password)
}