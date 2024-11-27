package com.keremkulac.journeylog.data.repository

import com.keremkulac.journeylog.data.local.dao.AverageFuelPriceDao
import com.keremkulac.journeylog.data.local.model.CompanyEntity
import javax.inject.Inject

class CompanyRepositoryImp @Inject constructor(
    private val averageFuelPriceDao: AverageFuelPriceDao
) : CompanyRepository {
    override suspend fun saveCompanies(companyList: List<CompanyEntity>) {
        averageFuelPriceDao.insertCompanies(companyList)
    }
}