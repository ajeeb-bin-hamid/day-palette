package com.day.palette.domain.usecase

import com.day.palette.domain.Errors
import com.day.palette.domain.GenericResult
import com.day.palette.domain.model.Holiday
import com.day.palette.domain.repository.RemoteRepository
import javax.inject.Inject

class GetCountryHolidaysUseCase @Inject constructor(private val remoteRepository: RemoteRepository) {
    suspend fun execute(countryCode: String): GenericResult<ArrayList<Holiday>, Errors.Network> {
        return remoteRepository.getCountryHolidays(countryCode)
    }

}