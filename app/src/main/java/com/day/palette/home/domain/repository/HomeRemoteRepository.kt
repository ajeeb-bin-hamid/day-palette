package com.day.palette.home.domain.repository

import com.day.palette.home.domain.model.Country
import com.day.palette.home.domain.model.Holiday
import com.day.palette.common.domain.utils.Errors
import com.day.palette.common.domain.utils.GenericResult

interface HomeRemoteRepository {
    suspend fun getCountryHolidays(countryCode: String): GenericResult<ArrayList<Holiday>, Errors.Network>
    suspend fun getAllCountries(): GenericResult<List<Country>, Errors.Network>
    suspend fun getWorldWideHolidays(): GenericResult<ArrayList<Holiday>, Errors.Network>
}