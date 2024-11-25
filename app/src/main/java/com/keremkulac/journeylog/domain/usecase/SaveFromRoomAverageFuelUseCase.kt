package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.data.local.model.AverageFuelPriceEntity
import com.keremkulac.journeylog.data.repository.AverageFuelPriceImp
import javax.inject.Inject

class SaveFromRoomAverageFuelUseCase @Inject constructor(private val averageFuelPriceImp: AverageFuelPriceImp) {
    suspend fun invoke(averageFuelPriceEntityList: List<AverageFuelPriceEntity>) =
        averageFuelPriceImp.saveFuelPrices(averageFuelPriceEntityList)
}