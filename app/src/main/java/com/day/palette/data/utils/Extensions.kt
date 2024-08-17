package com.day.palette.data.utils

import com.day.palette.domain.Errors
import com.day.palette.domain.GenericResult
import retrofit2.Response
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.HttpURLConnection.HTTP_OK
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/***/
inline fun <T> safeApiCall(
    apiCall: () -> Response<T>
): GenericResult<T, Errors.Network> {
    return try {
        val response = apiCall()
        val body = response.body() ?: return GenericResult.Error(Errors.Network.NO_RESPONSE)

        when (response.code()) {
            HTTP_OK -> GenericResult.Success(body)
            HTTP_INTERNAL_ERROR -> GenericResult.Error(Errors.Network.INTERNAL_ERROR)
            else -> GenericResult.Error(Errors.Network.UNKNOWN)
        }
    } catch (e: UnknownHostException) {
        GenericResult.Error(Errors.Network.NO_INTERNET)
    } catch (e: SocketTimeoutException) {
        GenericResult.Error(Errors.Network.REQUEST_TIMEOUT)
    } catch (e: Exception) {
        GenericResult.Error(Errors.Network.UNKNOWN)
    }
}
