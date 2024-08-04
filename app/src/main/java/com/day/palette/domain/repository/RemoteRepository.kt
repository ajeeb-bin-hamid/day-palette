package com.day.palette.domain.repository

import com.day.palette.domain.model.Holiday
import com.day.palette.GenericResult

interface RemoteRepository {
    suspend fun getCountryHolidays(countryCode: String): GenericResult<List<Holiday>?>
}