package com.keremkulac.journeylog.util

import android.content.res.Resources

object ResourceUtil {
    fun getResourceId(resources: Resources, name: String, packageName: String): Int {
        return resources.getIdentifier(name, "drawable", packageName)
    }
}