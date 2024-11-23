package com.keremkulac.journeylog.domain.usecase

import com.keremkulac.journeylog.domain.repository.FuelOilApiRepositoryImp
import com.keremkulac.journeylog.util.Result
import javax.inject.Inject

class GetFuelOilPricesUseCase @Inject constructor(
    private val fuelOilApiRepositoryImp: FuelOilApiRepositoryImp
) {
    suspend operator fun invoke(city : String,result: (Result<Any>) -> Unit) {
        fuelOilApiRepositoryImp.getFuelOilPrices(city) {
            result.invoke(it)
        }
    }

}