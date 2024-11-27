package com.keremkulac.journeylog.data.repository

import com.keremkulac.journeylog.data.local.dao.CompanyDao
import com.keremkulac.journeylog.data.local.model.CompanyEntity
import javax.inject.Inject

class CompanyRepositoryImp @Inject constructor(
    private val companyDao: CompanyDao
) : CompanyRepository {
    override suspend fun saveCompanies(companyList: List<CompanyEntity>) {
        companyDao.insertCompanies(companyList)
    }

    override suspend fun getCompanies(): List<CompanyEntity> {
        return companyDao.getCompanies()
    }

    override suspend fun deleteAllCompanies() {
        companyDao.deleteCompanies()
    }
}