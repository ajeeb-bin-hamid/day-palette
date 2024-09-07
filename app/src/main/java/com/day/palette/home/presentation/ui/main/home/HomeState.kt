package com.day.palette.home.presentation.ui.main.home

import android.os.Parcelable
import com.day.palette.home.domain.model.Country
import com.day.palette.home.domain.model.Holiday
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeState(
    val selectedCountryName: String = "",
    val selectedCountryCode: String = "",
    val countryHolidays: ArrayList<Holiday> = ArrayList(),
    val allCountries: List<Country> = emptyList(),
) : Parcelable