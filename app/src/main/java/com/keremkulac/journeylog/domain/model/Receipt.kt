package com.keremkulac.journeylog.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Receipt(
    var id: String = "",
    var email: String = "",
    var stationName: String = "",
    var fuelType: String = "",
    var literPrice: String = "",
    var liter: String = "",
    var vehicleLicensePlate: String = "",
    var vehicleLastKm: String = "",
    var tax: String = "",
    var total: String = "",
    var date: String = "",
    var time: String = ""
) : Parcelable