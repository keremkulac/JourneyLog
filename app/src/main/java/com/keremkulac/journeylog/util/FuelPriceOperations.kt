package com.keremkulac.journeylog.util

import com.keremkulac.journeylog.domain.model.DistrictData
import com.keremkulac.journeylog.domain.model.FuelPrice
import java.util.Locale
import javax.inject.Inject

class FuelPriceOperations @Inject constructor() {

    fun calculateAveragePrice(fuelPriceList: List<DistrictData>, fuelType: FuelType): Double {
        var emptyCount = 0
        var pricesCount = 0
        var total = 0.0
        for (data in fuelPriceList) {
            for (price in data.prices) {
                val selectedPrice = getFuelType(fuelType, price)
                if (selectedPrice != "-") {
                    total += selectedPrice
                        .replace("₺", "")
                        .replace(",", ".").toDouble()
                } else {
                    emptyCount++
                }
                pricesCount++
            }
        }
        total = String.format(Locale.US, "%.2f", (total / (pricesCount - emptyCount))).toDouble()
        return total
    }

    private fun getFuelType(fuelType: FuelType, price: FuelPrice): String {
        return when (fuelType) {
            FuelType.GASOLINE -> price.gasoline
            FuelType.DIESEL -> price.diesel
            FuelType.LPG -> price.lpg
        }
    }

}