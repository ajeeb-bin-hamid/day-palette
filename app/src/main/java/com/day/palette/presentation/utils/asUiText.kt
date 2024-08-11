package com.day.palette.presentation.utils

import com.day.palette.R
import com.day.palette.domain.Errors
import com.day.palette.domain.GenericError

fun GenericError.asUiText(): UiText {
    return when (this) {
        /** Errors that can occur while performing network calls */
        Errors.Network.NO_RESPONSE -> UiText.StringResource(R.string.error_no_response)
        Errors.Network.NO_INTERNET -> UiText.StringResource(R.string.error_no_internet)
        Errors.Network.REQUEST_TIMEOUT -> UiText.StringResource(R.string.error_request_timeout)
        Errors.Network.PAYLOAD_TOO_LARGE -> UiText.StringResource(R.string.error_payload_too_large)
        Errors.Network.UNKNOWN -> UiText.StringResource(R.string.error_unknown)

        /** Errors that can occur while performing operations on SharedPreferences */
        Errors.Prefs.NO_SUCH_DATA -> UiText.StringResource(R.string.error_not_data_found)
        Errors.Prefs.UNKNOWN -> UiText.StringResource(R.string.error_unknown)

    }
}