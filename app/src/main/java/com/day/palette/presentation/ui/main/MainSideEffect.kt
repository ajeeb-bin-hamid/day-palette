package com.day.palette.presentation.ui.main

import com.day.palette.presentation.utils.UiText

sealed class MainSideEffect {
    /**Actions that can be performed on the UI*/
    data class ShowToast(val message: UiText) : MainSideEffect()
    data class ShowSnack(val message: UiText) : MainSideEffect()

    /**Actions that can be used to invoke functions on the ViewModel*/
}
