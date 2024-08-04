package com.day.palette.data.repository

import com.day.palette.data.network.ApiService
import com.day.palette.domain.model.Holiday
import com.day.palette.domain.repository.RemoteRepository
import com.day.palette.GenericResult
import java.net.HttpURLConnection.HTTP_OK
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    RemoteRepository {
    override suspend fun getCountryHolidays(countryCode: String): GenericResult<List<Holiday>?> {
        val response = apiService.getCountryHolidays(countryCode)

        return when (response.code()) {
            HTTP_OK -> GenericResult.Success(response.body())
            else -> GenericResult.Error(Exception(response.message()))
        }

    }

}