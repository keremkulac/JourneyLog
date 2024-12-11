package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.repository.FirestoreRepositoryImp
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class GetAverageFuelPriceUseCase @Inject constructor(
    private val firestoreRepositoryImp: FirestoreRepositoryImp
) {
    suspend operator fun invoke(result: (Result<Any>) -> Unit) {
        firestoreRepositoryImp.getAverageFuelPrice {
            result.invoke(it)
        }
    }

}