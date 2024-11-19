package com.keremkulac.journeylog.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String = "",
    var name: String = "",
    var surname: String = "",
    var email: String = "",
    var imageUri: String = ""
) : Parcelable