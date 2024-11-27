package com.keremkulac.journeylog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keremkulac.journeylog.data.local.model.AverageFuelPriceEntity
import com.keremkulac.journeylog.data.local.model.CompanyEntity

@Dao
interface AverageFuelPriceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAverageFuelPrices(prices: List<AverageFuelPriceEntity>)

    @Query("SELECT * FROM average_fuel_price")
    suspend fun getAverageFuelPrices(): List<AverageFuelPriceEntity>

    @Query("DELETE FROM average_fuel_price")
    suspend fun deleteAllAverageFuelPrices()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanies(companyList: List<CompanyEntity>)

}