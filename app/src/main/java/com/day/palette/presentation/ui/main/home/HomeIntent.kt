package com.day.palette.presentation.ui.main.home

import com.day.palette.domain.model.SelectedCountryDetails

sealed class HomeIntent {
    data object GetCountryHolidays : HomeIntent()
    data object GetAllCountries : HomeIntent()
    data class SetSelectedCountry(val selectedCountryDetails: SelectedCountryDetails) : HomeIntent()
}