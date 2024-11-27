package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.data.repository.AverageFuelPriceRepositoryImp
import javax.inject.Inject

class DeleteAllAverageFuelPricesUseCase @Inject constructor(
    private val averageFuelPriceImp: AverageFuelPriceRepositoryImp
) {
    suspend fun invoke() = averageFuelPriceImp.deleteAllAverageFuelPrices()
}