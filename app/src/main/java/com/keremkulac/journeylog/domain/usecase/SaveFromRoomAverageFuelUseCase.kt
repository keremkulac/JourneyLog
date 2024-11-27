package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.data.local.model.AverageFuelPriceEntity
import com.keremkulac.journeylog.data.repository.AverageFuelPriceRepositoryImp
import javax.inject.Inject

class SaveFromRoomAverageFuelUseCase @Inject constructor(private val averageFuelPriceImp: AverageFuelPriceRepositoryImp) {
    suspend fun invoke(averageFuelPriceEntityList: List<AverageFuelPriceEntity>) =
        averageFuelPriceImp.saveFuelPrices(averageFuelPriceEntityList)
}