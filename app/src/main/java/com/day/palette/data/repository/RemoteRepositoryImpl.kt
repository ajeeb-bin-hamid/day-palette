package com.day.palette.data.repository

import com.day.palette.data.network.ApiService
import com.day.palette.data.utils.safeApiCall
import com.day.palette.domain.model.Country
import com.day.palette.domain.model.Holiday
import com.day.palette.domain.repository.RemoteRepository
import com.day.palette.domain.utils.Errors.Network
import com.day.palette.domain.utils.GenericResult
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    RemoteRepository {

    override suspend fun getCountryHolidays(countryCode: String): GenericResult<ArrayList<Holiday>, Network> =
        safeApiCall { apiService.getCountryHolidays(countryCode) }

    override suspend fun getAllCountries(): GenericResult<List<Country>, Network> =
        safeApiCall { apiService.getAllCountries() }

    override suspend fun getWorldWideHolidays(): GenericResult<ArrayList<Holiday>, Network> =
        safeApiCall { apiService.getWorldWideHolidays() }
}