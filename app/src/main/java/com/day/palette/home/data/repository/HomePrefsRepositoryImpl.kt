package com.day.palette.home.data.repository

import com.day.palette.home.data.prefs.HomePrefsHelper
import com.day.palette.common.domain.utils.Errors
import com.day.palette.common.domain.utils.GenericResult
import com.day.palette.home.domain.model.SelectedCountryDetails
import com.day.palette.home.domain.repository.HomePrefsRepository
import javax.inject.Inject

class HomePrefsRepositoryImpl @Inject constructor(private val homePrefsHelper: HomePrefsHelper) :
    HomePrefsRepository {

    override fun getSelectedCountryDetails(): GenericResult<SelectedCountryDetails, Errors.Prefs> {
        val countryName = homePrefsHelper.selectedCountryName
        val countryCode = homePrefsHelper.selectedCountryCode

        if (countryName != null && countryCode != null) return GenericResult.Success(
            SelectedCountryDetails(countryName, countryCode)
        )
        return GenericResult.Error(Errors.Prefs.NO_SUCH_DATA)
    }

    override fun setSelectedCountryDetails(selectedCountryDetails: SelectedCountryDetails): GenericResult<Unit, Errors.Prefs> {
        try {
            homePrefsHelper.selectedCountryCode = selectedCountryDetails.selectedCountryCode
            homePrefsHelper.selectedCountryName = selectedCountryDetails.selectedCountryName
            return GenericResult.Success(Unit)
        } catch (e: Exception) {
            return GenericResult.Error(Errors.Prefs.UNKNOWN)
        }
    }

}