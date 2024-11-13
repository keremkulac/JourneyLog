package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.repository.AuthRepositoryImp
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val authRepositoryImp: AuthRepositoryImp
) {
    suspend fun invoke(id : String,result: (Result<Any>) -> Unit){
        authRepositoryImp.getUser(id){
            result.invoke(it)
        }
    }
}