package com.keremkulac.journeylog.domain.repository

import com.keremkulac.journeylog.util.Result

interface FuelOilApiRepository {

    suspend fun getFuelOilPrices(city : String,result: (Result<Any>) -> Unit)
}