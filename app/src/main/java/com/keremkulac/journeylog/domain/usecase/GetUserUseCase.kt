package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.repository.FirestoreRepository
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend fun invoke(id : String,result: (Result<Any>) -> Unit){
        firestoreRepository.getUser(id){
            result.invoke(it)
        }
    }
}