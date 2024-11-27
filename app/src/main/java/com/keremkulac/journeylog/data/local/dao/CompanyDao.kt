package com.keremkulac.journeylog.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keremkulac.journeylog.data.local.model.CompanyEntity

@Dao
interface CompanyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanies(companyList: List<CompanyEntity>)

    @Query("SELECT * FROM company")
    suspend fun getCompanies(): List<CompanyEntity>

    @Query("DELETE FROM company")
    suspend fun deleteCompanies()

}