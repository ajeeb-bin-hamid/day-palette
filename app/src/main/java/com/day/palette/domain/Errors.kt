package com.day.palette.domain

sealed interface Errors : GenericError {

    /**Errors that can occur while performing network calls*/
    enum class Network : Errors {
        NO_RESPONSE, NO_INTERNET, REQUEST_TIMEOUT, PAYLOAD_TOO_LARGE, UNKNOWN
    }

    /**Errors that can occur while performing operations on SharedPreferences*/
    enum class Prefs : Errors {
        NO_SUCH_DATA, UNKNOWN
    }
}