package com.day.palette.home.domain.usecase

import com.day.palette.home.domain.model.SelectedCountryDetails
import com.day.palette.home.domain.repository.HomePrefsRepository
import com.day.palette.common.domain.utils.Errors
import com.day.palette.common.domain.utils.GenericResult
import javax.inject.Inject

class GetSelectedCountryDetailsUseCase @Inject constructor(private val homePrefsRepository: HomePrefsRepository) {
    fun execute(): GenericResult<SelectedCountryDetails, Errors.Prefs> {
        return when (val getSelectedCountryCall = homePrefsRepository.getSelectedCountryDetails()) {
            is GenericResult.Success -> getSelectedCountryCall
            is GenericResult.Error -> GenericResult.Success(
                SelectedCountryDetails(
                    selectedCountryName = DEFAULT_COUNTRY_NAME,
                    selectedCountryCode = DEFAULT_COUNTRY_CODE
                )
            )
        }
    }

    companion object {
        const val DEFAULT_COUNTRY_NAME = "United States"
        const val DEFAULT_COUNTRY_CODE = "US"
    }
}