package com.keremkulac.journeylog.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.util.Locale

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun String.decimalFormat() : String{
    return String.format(Locale.getDefault(), "%,.2f", this.toDouble()).replace(",", ".")
}

fun Double.toMoneyFormat(): String {
    return try {
        String.format(Locale("tr", "TR"), "%,.2f", this)
            .replace(".", "x")
            .replace(".", ",")
            .replace("x", ".")
    } catch (e: Exception) {
        this.toString()
    }
}