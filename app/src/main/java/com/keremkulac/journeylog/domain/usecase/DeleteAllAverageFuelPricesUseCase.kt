package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.data.repository.AverageFuelPriceImp
import javax.inject.Inject

class DeleteAllAverageFuelPricesUseCase @Inject constructor(
    private val averageFuelPriceImp: AverageFuelPriceImp
) {
    suspend fun invoke() = averageFuelPriceImp.deleteAllAverageFuelPrices()
}