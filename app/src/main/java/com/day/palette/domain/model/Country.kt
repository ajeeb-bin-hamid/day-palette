package com.day.palette.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    @SerializedName("countryCode") val countryCode: String = "",
    @SerializedName("name") val name: String = ""
) : Parcelable