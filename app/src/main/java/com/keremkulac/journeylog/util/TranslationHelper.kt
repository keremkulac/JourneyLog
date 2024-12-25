package com.keremkulac.journeylog.util

import LocaleHelper
import android.content.Context
import javax.inject.Inject

class TranslationHelper @Inject constructor(private val context: Context) {

    sealed class TranslationType(val translations: Map<String, String>) {
        object Fuel : TranslationType(
            mapOf(
                "Benzin" to "Gasoline",
                "LPG" to "LPG",
                "Dizel" to "Diesel"
            )
        )

        object Vehicle : TranslationType(
            mapOf(
                "Otomobil" to "Automobile",
                "Motorsiklet" to "Motorcycle",
                "SUV" to "SUV",
                "Ticari" to "Van",
                "Kamyonet" to "Truck"
            )
        )
    }

    fun translate(text: String, type: TranslationType): String {
        return if (isEnglishLocale()) {
            type.translations[text] ?: text
        } else text
    }

    private fun isEnglishLocale(): Boolean {
        return LocaleHelper.loadLanguagePreference(context) == "en"
    }
}