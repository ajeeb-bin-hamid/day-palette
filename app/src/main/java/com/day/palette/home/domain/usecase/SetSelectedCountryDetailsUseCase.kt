package com.day.palette.home.domain.usecase

import com.day.palette.common.domain.utils.Errors
import com.day.palette.common.domain.utils.GenericResult
import com.day.palette.home.domain.model.SelectedCountryDetails
import com.day.palette.home.domain.repository.HomePrefsRepository
import javax.inject.Inject

class SetSelectedCountryDetailsUseCase @Inject constructor(private val homePrefsRepository: HomePrefsRepository) {
    fun execute(selectedCountryDetails: SelectedCountryDetails): GenericResult<Unit, Errors.Prefs> {
        return homePrefsRepository.setSelectedCountryDetails(selectedCountryDetails)
    }
}