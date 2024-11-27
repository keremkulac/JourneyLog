package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.data.local.model.CompanyEntity
import com.keremkulac.journeylog.data.repository.CompanyRepositoryImp
import javax.inject.Inject

class SaveFromRoomCompanyUseCase @Inject constructor(
    private val companyRepositoryImp: CompanyRepositoryImp
) {
    suspend fun invoke(list: List<CompanyEntity>){
        companyRepositoryImp.saveCompanies(list)

    }
}