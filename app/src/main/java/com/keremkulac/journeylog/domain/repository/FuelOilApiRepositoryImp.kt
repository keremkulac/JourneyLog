package com.keremkulac.journeylog.domain.repository

import com.keremkulac.journeylog.domain.ApiService
import com.keremkulac.journeylog.domain.model.FuelPricesResponse
import com.keremkulac.journeylog.util.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class FuelOilApiRepositoryImp @Inject constructor(
    private val apiService: ApiService
) : FuelOilApiRepository {

    override suspend fun getFuelOilPrices(city : String,result: (Result<Any>) -> Unit) {
        try {
            val call = apiService.getFuelPrices(city)
            call.enqueue(object : Callback<FuelPricesResponse> {
                override fun onResponse(
                    call: Call<FuelPricesResponse>,
                    response: Response<FuelPricesResponse>
                ) {
                    val data = response.body()?.data ?: emptyList()
                    result.invoke(Result.Success(data))
                }

                override fun onFailure(call: Call<FuelPricesResponse>, t: Throwable) {
                    result.invoke(Result.Failure(t.message.toString()))
                }

            })
        } catch (e: Exception) {
            result.invoke(Result.Failure(e.message.toString()))
        }
    }
}