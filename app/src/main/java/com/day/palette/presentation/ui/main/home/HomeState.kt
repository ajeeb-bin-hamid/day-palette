package com.day.palette.presentation.ui.main.home

import android.os.Parcelable
import com.day.palette.domain.model.Holiday
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeState(
    val selectedCountryName: String,
    val selectedCountryCode: String,
    val countryHolidays: List<Holiday>,
    val isLoading: Boolean
) : Parcelable