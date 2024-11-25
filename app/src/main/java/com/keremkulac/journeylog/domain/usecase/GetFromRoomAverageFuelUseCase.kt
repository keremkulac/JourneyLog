package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.data.repository.AverageFuelPriceImp
import javax.inject.Inject

class GetFromRoomAverageFuelUseCase @Inject constructor(
    private val averageFuelPriceImp: AverageFuelPriceImp
) {
    suspend fun invoke() = averageFuelPriceImp.getFuelPrices()
}