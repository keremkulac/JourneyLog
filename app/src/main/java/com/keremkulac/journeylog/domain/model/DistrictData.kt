package com.keremkulac.journeylog.domain.model

data class DistrictData(
    val district: String,
    val prices: List<FuelPrice>
)