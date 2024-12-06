package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.domain.repository.FirestoreRepositoryImp
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class SaveVehicleUseCase @Inject constructor(
    private val firestoreRepositoryImp: FirestoreRepositoryImp
) {
    suspend fun invoke(vehicle: Vehicle, result: (Result<String>) -> Unit) {
        firestoreRepositoryImp.saveVehicle(vehicle) {
            result.invoke(it)
        }
    }
}