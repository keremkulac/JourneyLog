package com.keremkulac.journeylog.domain

import com.keremkulac.journeylog.domain.model.FuelPricesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {

    @GET("fuel/{city}")
    fun getFuelPrices(@Path ("city") city: String): Call<FuelPricesResponse>
}