package com.day.palette.data.repository

import com.day.palette.data.prefs.SharedPrefsHelper
import com.day.palette.domain.Errors
import com.day.palette.domain.GenericResult
import com.day.palette.domain.model.SelectedCountryDetails
import com.day.palette.domain.repository.UserPrefsRepository
import javax.inject.Inject

class UserPrefsRepositoryImpl @Inject constructor(private val sharedPrefsHelper: SharedPrefsHelper) :
    UserPrefsRepository {

    override fun getSelectedCountryDetails(): GenericResult<SelectedCountryDetails, Errors.Prefs> {
        val countryName = sharedPrefsHelper.selectedCountryName
        val countryCode = sharedPrefsHelper.selectedCountryCode

        if (countryName != null && countryCode != null) return GenericResult.Success(
            SelectedCountryDetails(countryName, countryCode)
        )
        return GenericResult.Error(Errors.Prefs.NO_SUCH_DATA)
    }

    override fun setSelectedCountryDetails(selectedCountryDetails: SelectedCountryDetails): GenericResult<Unit, Errors.Prefs> {
        try {
            sharedPrefsHelper.selectedCountryCode = selectedCountryDetails.selectedCountryCode
            sharedPrefsHelper.selectedCountryName = selectedCountryDetails.selectedCountryName
            return GenericResult.Success(Unit)
        } catch (e: Exception) {
            return GenericResult.Error(Errors.Prefs.UNKNOWN)
        }
    }

}