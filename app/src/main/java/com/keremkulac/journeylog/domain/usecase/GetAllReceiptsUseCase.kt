package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.repository.FirestoreRepositoryImp
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class GetAllReceiptsUseCase @Inject constructor(
    private val firestoreRepositoryImp: FirestoreRepositoryImp
) {
    suspend fun invoke(result: (Result<Any>) -> Unit) {
        firestoreRepositoryImp.getAllReceipts {
            result.invoke(it)
        }
    }
}