package com.keremkulac.journeylog.domain.model

data class FuelPricesResponse(
    val data: List<DistrictData>,
    val message: String,
    val status: String
)