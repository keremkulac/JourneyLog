package com.keremkulac.journeylog.util

import com.keremkulac.journeylog.data.local.model.CompanyEntity
import com.keremkulac.journeylog.domain.model.DistrictData
import com.keremkulac.journeylog.domain.model.FuelPrice
import java.util.Locale
import javax.inject.Inject

class FuelPriceOperations @Inject constructor() {

    fun calculateAveragePrice(
        fuelPriceList: List<DistrictData>,
        fuelType: FuelType,
        companyName: String = ""
    ): Double {
        var emptyCount = 0
        var pricesCount = 0
        var total = 0.0
        for (data in fuelPriceList) {
            for (price in data.prices) {
                val selectedPrice = getFuelType(fuelType, price)
                if (selectedPrice != "-" && (companyName.isEmpty() || price.companyName == companyName)) {
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

    fun getCompanyList(fuelPriceList: List<DistrictData>): List<CompanyEntity> {
        val companyList = mutableSetOf<CompanyEntity>()
        for (fuelPrice in fuelPriceList) {
            for (price in fuelPrice.prices) {
                val companyEntity = CompanyEntity(0, price.companyName)
                companyList.add(companyEntity)
            }
        }
        return companyList.toList()
    }

}