package com.keremkulac.journeylog.util

object TranslationHelper {
    private val translationMap = mapOf(
        "Benzin" to "Gasoline",
        "LPG" to "LPG",
        "Dizel" to "Diesel",

    )

    fun translateManually(text: String): String {
        return translationMap[text] ?: text
    }
}