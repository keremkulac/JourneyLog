package com.keremkulac.journeylog.data.repository

import com.keremkulac.journeylog.data.local.dao.AverageFuelPriceDao
import com.keremkulac.journeylog.data.local.model.AverageFuelPriceEntity
import javax.inject.Inject

class AverageFuelPriceRepositoryImp @Inject constructor(
    private val averageFuelPriceDao: AverageFuelPriceDao) : AverageFuelPriceRepository {

    override suspend fun saveFuelPrices(averageFuelPriceEntityList: List<AverageFuelPriceEntity>) {
        averageFuelPriceDao.insertAverageFuelPrices(averageFuelPriceEntityList)
    }

    override suspend fun getFuelPrices(): List<AverageFuelPriceEntity> {
        return averageFuelPriceDao.getAverageFuelPrices()
    }

    override suspend fun deleteAllAverageFuelPrices() {
        averageFuelPriceDao.deleteAllAverageFuelPrices()
    }
}