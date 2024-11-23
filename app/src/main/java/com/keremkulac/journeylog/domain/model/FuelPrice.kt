package com.keremkulac.journeylog.domain.model

import com.google.gson.annotations.SerializedName

data class FuelPrice(
    @SerializedName("dagitici_firma")
    val companyName: String,
    @SerializedName("benzin")
    val gasoline: String,
    @SerializedName("motorin")
    val diesel: String,
    @SerializedName("lpg")
    val lpg: String,
    @SerializedName("guncellenme_tarihi")
    val updateDate: String
)