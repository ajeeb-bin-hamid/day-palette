package com.day.palette.home.domain.usecase

import com.day.palette.common.domain.utils.Errors
import com.day.palette.common.domain.utils.GenericResult
import com.day.palette.home.domain.model.Country
import com.day.palette.home.domain.repository.HomeRemoteRepository
import javax.inject.Inject

class GetAllCountriesUseCase @Inject constructor(private val homeRemoteRepository: HomeRemoteRepository) {
    suspend fun execute(): GenericResult<List<Country>, Errors.Network> {
        return homeRemoteRepository.getAllCountries()
    }
}