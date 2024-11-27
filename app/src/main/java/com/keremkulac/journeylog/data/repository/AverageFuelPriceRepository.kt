package com.keremkulac.journeylog.data.repository

import com.keremkulac.journeylog.data.local.model.AverageFuelPriceEntity

interface AverageFuelPriceRepository {

    suspend fun saveFuelPrices(averageFuelPriceEntityList: List<AverageFuelPriceEntity>)

    suspend fun getFuelPrices() : List<AverageFuelPriceEntity>

    suspend fun deleteAllAverageFuelPrices()
}