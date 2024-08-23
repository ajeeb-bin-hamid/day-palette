package com.day.palette.domain.repository

import com.day.palette.domain.model.Country
import com.day.palette.domain.model.Holiday
import com.day.palette.domain.utils.Errors
import com.day.palette.domain.utils.GenericResult

interface RemoteRepository {
    suspend fun getCountryHolidays(countryCode: String): GenericResult<ArrayList<Holiday>, Errors.Network>
    suspend fun getAllCountries(): GenericResult<List<Country>, Errors.Network>
    suspend fun getWorldWideHolidays(): GenericResult<ArrayList<Holiday>, Errors.Network>
}