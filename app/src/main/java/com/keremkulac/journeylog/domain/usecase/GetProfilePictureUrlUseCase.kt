package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.repository.FirestoreRepositoryImp
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class GetProfilePictureUrlUseCase @Inject constructor(
    private val firestoreRepositoryImp: FirestoreRepositoryImp
) {
    suspend fun invoke(path : String, result: (Result<String>) -> Unit) {
        firestoreRepositoryImp.getProfilePictureUrl(path){
            result.invoke(it)
        }
    }
}