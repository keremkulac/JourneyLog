package com.keremkulac.journeylog.util

import android.content.Context
import javax.inject.Inject

class LastUpdateSharedPreferences @Inject constructor(private val context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("last_update_shared_pref", Context.MODE_PRIVATE)

    fun saveData(time: Long) {
        val editor = sharedPreferences.edit()
        editor.putString("time", time.toString())
        editor.apply()
    }

    fun getData(): String? {
        return sharedPreferences.getString("time", null)
    }
}