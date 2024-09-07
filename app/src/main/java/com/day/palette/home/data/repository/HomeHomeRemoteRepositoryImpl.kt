package com.day.palette.home.data.repository

import com.day.palette.home.data.network.HomeApiService
import com.day.palette.common.data.utils.safeApiCall
import com.day.palette.home.domain.model.Country
import com.day.palette.home.domain.model.Holiday
import com.day.palette.home.domain.repository.HomeRemoteRepository
import com.day.palette.common.domain.utils.Errors.Network
import com.day.palette.common.domain.utils.GenericResult
import javax.inject.Inject

class HomeHomeRemoteRepositoryImpl @Inject constructor(private val homeApiService: HomeApiService) :
    HomeRemoteRepository {

    override suspend fun getCountryHolidays(countryCode: String): GenericResult<ArrayList<Holiday>, Network> =
        safeApiCall { homeApiService.getCountryHolidays(countryCode) }

    override suspend fun getAllCountries(): GenericResult<List<Country>, Network> =
        safeApiCall { homeApiService.getAllCountries() }

    override suspend fun getWorldWideHolidays(): GenericResult<ArrayList<Holiday>, Network> =
        safeApiCall { homeApiService.getWorldWideHolidays() }
}