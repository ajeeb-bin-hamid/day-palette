package com.day.palette.domain.repository

import com.day.palette.domain.Errors
import com.day.palette.domain.GenericResult
import com.day.palette.domain.model.Country
import com.day.palette.domain.model.Holiday

interface RemoteRepository {
    suspend fun getCountryHolidays(countryCode: String): GenericResult<List<Holiday>, Errors.Network>

    suspend fun getAllCountries(): GenericResult<List<Country>, Errors.Network>
}