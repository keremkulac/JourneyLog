package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.data.repository.CompanyRepositoryImp
import javax.inject.Inject

class DeleteAllCompaniesUseCase @Inject constructor(
    private val companyRepositoryImp: CompanyRepositoryImp
) {
    suspend fun invoke() {
        companyRepositoryImp.deleteAllCompanies()
    }
}