package com.keremkulac.journeylog.util

import LocaleHelper
import android.content.Context
import javax.inject.Inject

class TranslationHelper @Inject constructor(val context: Context) {
    private val translationMap = mapOf(
        "Benzin" to "Gasoline",
        "LPG" to "LPG",
        "Dizel" to "Diesel",
    )

    fun translateManually(text: String): String {
        val localeCode = LocaleHelper.loadLanguagePreference(context)
        if (localeCode == "en") {
            return translationMap[text] ?: text
        }
        return text
    }
}