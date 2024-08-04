package com.day.palette.domain.repository

import com.day.palette.domain.model.SelectedCountryDetails

interface UserPrefsRepository {
    fun getSelectedCountryDetails(): SelectedCountryDetails
}