package com.day.palette.domain.usecase

import com.day.palette.domain.utils.Errors
import com.day.palette.domain.utils.GenericResult
import com.day.palette.domain.model.Country
import com.day.palette.domain.repository.RemoteRepository
import javax.inject.Inject

class GetAllCountriesUseCase @Inject constructor(private val remoteRepository: RemoteRepository) {
    suspend fun execute(): GenericResult<List<Country>, Errors.Network> {
        return remoteRepository.getAllCountries()
    }
}