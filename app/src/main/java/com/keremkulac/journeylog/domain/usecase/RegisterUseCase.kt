package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.model.User
import com.keremkulac.journeylog.domain.repository.FirestoreRepositoryImp
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val firestoreRepositoryImp: FirestoreRepositoryImp) {
    suspend fun invoke(user: User, result: (Result<String>) -> Unit){
        firestoreRepositoryImp.registerUser(user){
            result.invoke(it)
        }
    }
}