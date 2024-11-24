package com.keremkulac.journeylog.util

object FuelTypeTranslation {
    private val translations = mapOf(
        "Gasoline" to "Benzin",
        "Diesel" to "Dizel",
        "LPG" to "LPG"
    )

    fun translateKey(key: String): String {
        return translations[key] ?: key
    }
}