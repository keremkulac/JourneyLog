package com.keremkulac.journeylog.util

import com.keremkulac.journeylog.domain.model.Vehicle

object VehicleItemsUtil {

    fun getVehicleItems(): List<Vehicle> {
        return listOf(
            Vehicle(
                iconName = "ic_bike",
                illustrationName = "illustration_motorcycle",
                title = "Motorsiklet"
            ),
            Vehicle(
                iconName = "ic_car",
                illustrationName = "illustration_car",
                title = "Otomobil"
            ),
            Vehicle(
                iconName = "ic_suv",
                illustrationName = "illustration_suv",
                title = "SUV"
            ),
            Vehicle(
                iconName = "ic_van",
                illustrationName = "illustration_van",
                title = "Ticari"
            ),
            Vehicle(
                iconName = "ic_truck",
                illustrationName = "illustration_truck",
                title = "Kamyonet"
            )
        )
    }
}

