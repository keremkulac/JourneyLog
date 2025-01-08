package com.keremkulac.journeylog.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatUtil {
    fun fullDateFormat() = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
}
