package com.keremkulac.journeylog.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vehicle(
    var id: String = "",
    var userId: String? = "",
    val iconName: String? = "",
    val illustrationName: String? = "",
    var lastKm: String? = "",
    var vehicleFuelType: String? = "",
    val title: String? = "",
    var licensePlate: String? = ""
) : Parcelable
