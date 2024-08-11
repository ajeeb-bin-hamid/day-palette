package com.day.palette.data.repository

import com.day.palette.data.network.ApiService
import com.day.palette.domain.Errors
import com.day.palette.domain.GenericResult
import com.day.palette.domain.model.Country
import com.day.palette.domain.model.Holiday
import com.day.palette.domain.repository.RemoteRepository
import java.net.HttpURLConnection.HTTP_OK
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    RemoteRepository {
    override suspend fun getCountryHolidays(countryCode: String): GenericResult<List<Holiday>, Errors.Network> {

        val response = apiService.getCountryHolidays(countryCode)
        val body = response.body() ?: return GenericResult.Error(Errors.Network.NO_RESPONSE)

        return when (response.code()) {
            HTTP_OK -> GenericResult.Success(body)
            else -> GenericResult.Error(Errors.Network.UNKNOWN)
        }

    }

    override suspend fun getAllCountries(): GenericResult<List<Country>, Errors.Network> {
        val response = apiService.getAllCountries()
        val body = response.body() ?: return GenericResult.Error(Errors.Network.NO_RESPONSE)

        return when (response.code()) {
            HTTP_OK -> GenericResult.Success(body)
            else -> GenericResult.Error(Errors.Network.UNKNOWN)
        }
    }

}