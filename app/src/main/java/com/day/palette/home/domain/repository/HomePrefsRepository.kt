package com.day.palette.home.domain.repository

import com.day.palette.common.domain.utils.Errors
import com.day.palette.common.domain.utils.GenericResult
import com.day.palette.home.domain.model.SelectedCountryDetails

interface HomePrefsRepository {
    fun getSelectedCountryDetails(): GenericResult<SelectedCountryDetails, Errors.Prefs>
    fun setSelectedCountryDetails(selectedCountryDetails: SelectedCountryDetails): GenericResult<Unit, Errors.Prefs>
}