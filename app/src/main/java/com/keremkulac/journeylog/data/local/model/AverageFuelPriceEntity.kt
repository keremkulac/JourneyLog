package com.keremkulac.journeylog.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "average_fuel_price")
data class AverageFuelPriceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val price: Double
)