package com.day.palette.domain.repository

import com.day.palette.domain.utils.Errors
import com.day.palette.domain.utils.GenericResult
import com.day.palette.domain.model.SelectedCountryDetails

interface UserPrefsRepository {
    fun getSelectedCountryDetails(): GenericResult<SelectedCountryDetails, Errors.Prefs>
    fun setSelectedCountryDetails(selectedCountryDetails: SelectedCountryDetails): GenericResult<Unit, Errors.Prefs>
}