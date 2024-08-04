package com.day.palette


sealed class GenericResult<out T> {
    data class Success<out T>(val data: T) : GenericResult<T>()
    data class Error(val e: Throwable) : GenericResult<Nothing>()
}