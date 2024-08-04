package com.day.palette.data.repository

import com.day.palette.data.prefs.SharedPrefsHelper
import com.day.palette.domain.model.SelectedCountryDetails
import com.day.palette.domain.repository.UserPrefsRepository
import javax.inject.Inject

class UserPrefsRepositoryImpl @Inject constructor(private val sharedPrefsHelper: SharedPrefsHelper) :
    UserPrefsRepository {

    override fun getSelectedCountryDetails(): SelectedCountryDetails {
        val countryName = sharedPrefsHelper.selectedCountryName
        val countryCode = sharedPrefsHelper.selectedCountryCode
        return SelectedCountryDetails(countryName, countryCode)
    }
}