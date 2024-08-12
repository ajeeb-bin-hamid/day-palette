package com.day.palette.domain.usecase

import com.day.palette.domain.Errors
import com.day.palette.domain.GenericResult
import com.day.palette.domain.model.SelectedCountryDetails
import com.day.palette.domain.repository.UserPrefsRepository
import javax.inject.Inject

class SetSelectedCountryDetailsUseCase @Inject constructor(private val userPrefsRepository: UserPrefsRepository) {
    fun execute(selectedCountryDetails: SelectedCountryDetails): GenericResult<Unit, Errors.Prefs> {
        return userPrefsRepository.setSelectedCountryDetails(selectedCountryDetails)
    }
}