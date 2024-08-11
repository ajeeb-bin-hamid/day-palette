package com.day.palette.presentation.ui.main

import com.day.palette.presentation.utils.UiText

sealed class MainIntent {
    /**Actions that can be invoked on the ViewModel*/

    /**Actions that can be performed on the UI*/
    data class ShowToast(val message: UiText) : MainIntent()
    data class ShowSnack(val message: UiText) : MainIntent()
}
