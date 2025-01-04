package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.model.Vehicle
import com.keremkulac.journeylog.domain.repository.FirestoreRepository
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class UpdateVehicleUseCase @Inject constructor(private val firestoreRepository: FirestoreRepository) {

    suspend operator fun invoke(vehicle: Vehicle, result: (Result<String>) -> Unit) {
        firestoreRepository.updateVehicle(vehicle){
            result.invoke(it)
        }
    }

}