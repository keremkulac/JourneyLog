package com.keremkulac.journeylog.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val imageUri: String = ""
) : Parcelable