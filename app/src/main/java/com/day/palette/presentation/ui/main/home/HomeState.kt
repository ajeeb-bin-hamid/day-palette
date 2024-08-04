package com.day.palette.presentation.ui.main.home

import com.day.palette.domain.model.Holiday

data class HomeState(
    val selectedCountryName: String,
    val selectedCountryCode: String,
    val countryHolidays: List<Holiday>,
    val isLoading: Boolean
)