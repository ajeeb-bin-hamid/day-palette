package com.day.palette.presentation.utils

import com.day.palette.R
import com.day.palette.domain.Errors.Network
import com.day.palette.domain.Errors.Prefs
import com.day.palette.domain.GenericError

fun GenericError.asUiText(): UiText {
    return when (this) {
        /** Errors that can occur while performing network calls */
        Network.NO_RESPONSE -> UiText.StringResource(R.string.error_no_response)
        Network.NO_INTERNET -> UiText.StringResource(R.string.error_no_internet)
        Network.REQUEST_TIMEOUT -> UiText.StringResource(R.string.error_request_timeout)
        Network.INTERNAL_ERROR -> UiText.StringResource(R.string.error_internal_error)
        Network.PAYLOAD_TOO_LARGE -> UiText.StringResource(R.string.error_payload_too_large)
        Network.UNKNOWN -> UiText.StringResource(R.string.error_unknown)

        /** Errors that can occur while performing operations on SharedPreferences */
        Prefs.NO_SUCH_DATA -> UiText.StringResource(R.string.error_not_data_found)
        Prefs.UNKNOWN -> UiText.StringResource(R.string.error_unknown)

    }
}