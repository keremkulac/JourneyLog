package com.keremkulac.journeylog.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company")
data class CompanyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val companyName: String,
)