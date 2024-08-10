package com.day.palette.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Holiday(
    @SerializedName("counties") val counties: List<String>? = null,
    @SerializedName("countryCode") val countryCode: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("fixed") val fixed: Boolean = false,
    @SerializedName("global") val global: Boolean = false,
    @SerializedName("launchYear") val launchYear: String? = null,
    @SerializedName("localName") val localName: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("types") val types: List<String> = listOf(),
    @SerializedName("bgColor") var bgColor: Int = 0
) : Parcelable