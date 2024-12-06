package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.repository.FirestoreRepositoryImp
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class GetAllVehiclesUseCase @Inject constructor(
    private val firestoreRepositoryImp: FirestoreRepositoryImp
) {
    suspend fun invoke(userId: String, result: (Result<Any>) -> Unit) {
        firestoreRepositoryImp.getAllVehicles(userId) {
            result(it)
        }
    }
}