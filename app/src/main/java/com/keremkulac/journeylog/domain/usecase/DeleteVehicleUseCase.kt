package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.repository.FirestoreRepository
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class DeleteVehicleUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {
    suspend fun invoke(vehicleId: String, result: (Result<String>) -> Unit) {
        firestoreRepository.deleteVehicle(vehicleId) {
            result.invoke(it)
        }
    }
}