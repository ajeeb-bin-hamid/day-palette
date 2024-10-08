package com.day.palette.data.network

import com.day.palette.domain.model.Country
import com.day.palette.domain.model.Holiday
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("NextPublicHolidays/{code}")
    suspend fun getCountryHolidays(@Path("code") countryCode: String): Response<ArrayList<Holiday>>

    @GET("AvailableCountries")
    suspend fun getAllCountries(): Response<List<Country>>

    @GET("NextPublicHolidaysWorldwide")
    suspend fun getWorldWideHolidays(): Response<ArrayList<Holiday>>
}