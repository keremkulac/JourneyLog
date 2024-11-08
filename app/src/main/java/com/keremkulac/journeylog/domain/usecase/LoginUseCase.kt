package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.repository.AuthRepositoryImp
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repositoryImp: AuthRepositoryImp) {
    suspend fun invoke(email : String, password : String) = repositoryImp.loginUser(email,password)
}