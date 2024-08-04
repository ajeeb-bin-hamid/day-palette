package com.day.palette.domain.usecase

import com.day.palette.domain.model.SelectedCountryDetails
import com.day.palette.domain.repository.UserPrefsRepository
import javax.inject.Inject

class GetSelectedCountryDetailsUseCase @Inject constructor(private val userPrefsRepository: UserPrefsRepository) {
    fun execute(): SelectedCountryDetails {
        return userPrefsRepository.getSelectedCountryDetails()
    }
}