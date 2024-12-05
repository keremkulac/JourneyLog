package com.keremkulac.journeylog.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vehicle(
    val iconResId: Int? = 0,
    val title: String? = ""
) : Parcelable
