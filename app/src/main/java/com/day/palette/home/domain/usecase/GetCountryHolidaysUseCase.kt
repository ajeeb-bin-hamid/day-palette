package com.day.palette.home.domain.usecase

import com.day.palette.common.domain.utils.Errors
import com.day.palette.common.domain.utils.GenericResult
import com.day.palette.home.domain.model.Holiday
import com.day.palette.home.domain.repository.HomeRemoteRepository
import javax.inject.Inject

class GetCountryHolidaysUseCase @Inject constructor(private val homeRemoteRepository: HomeRemoteRepository) {
    suspend fun execute(countryCode: String): GenericResult<ArrayList<Holiday>, Errors.Network> {
        return homeRemoteRepository.getCountryHolidays(countryCode)
    }

}