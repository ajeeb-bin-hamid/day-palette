package com.day.palette.home.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    @SerializedName("countryCode") val code: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("isSelected") var isSelected: Boolean = false
) : Parcelable