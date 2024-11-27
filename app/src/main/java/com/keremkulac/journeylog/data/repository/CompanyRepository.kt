package com.keremkulac.journeylog.data.repository

import com.keremkulac.journeylog.data.local.model.CompanyEntity

interface CompanyRepository {
    suspend fun saveCompanies(companyList: List<CompanyEntity>)

    suspend fun getCompanies() : List<CompanyEntity>

    suspend fun deleteAllCompanies()
}