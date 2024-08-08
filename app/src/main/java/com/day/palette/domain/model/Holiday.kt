package com.day.palette.domain.model

import com.google.gson.annotations.SerializedName

data class Holiday(
    @SerializedName("counties") val counties: List<String>? = null,
    @SerializedName("countryCode") val countryCode: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("fixed") val fixed: Boolean = false,
    @SerializedName("global") val global: Boolean = false,
    @SerializedName("launchYear") val launchYear: Any? = null,
    @SerializedName("localName") val localName: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("types") val types: List<String> = listOf(),
    @SerializedName("bgColor") var bgColor: Int = 0
)