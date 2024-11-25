package com.keremkulac.journeylog.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.keremkulac.journeylog.data.local.dao.AverageFuelPriceDao
import com.keremkulac.journeylog.data.local.model.AverageFuelPriceEntity


@Database(entities = [AverageFuelPriceEntity::class], version = 1)
abstract class AverageFuelPriceDatabase : RoomDatabase() {
    abstract fun averageFuelPriceDao(): AverageFuelPriceDao
}