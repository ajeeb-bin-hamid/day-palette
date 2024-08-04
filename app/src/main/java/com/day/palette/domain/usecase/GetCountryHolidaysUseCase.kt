package com.day.palette.domain.usecase

import com.day.palette.domain.model.Holiday
import com.day.palette.domain.repository.RemoteRepository
import com.day.palette.GenericResult
import javax.inject.Inject

class GetCountryHolidaysUseCase @Inject constructor(private val remoteRepository: RemoteRepository) {
    suspend fun execute(countryCode: String): GenericResult<List<Holiday>?> {
        return remoteRepository.getCountryHolidays(countryCode)
    }
}