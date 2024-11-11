package com.keremkulac.journeylog.domain.model

data class Receipt(
    val id : String,
    val email : String,
    val stationName : String,
    val fuelType: String,
    val literPrice: String,
    val liter : String,
    val tax : String,
    val total : String,
    val date: String,
    val time: String
)