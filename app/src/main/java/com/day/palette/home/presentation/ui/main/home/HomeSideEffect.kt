package com.day.palette.home.presentation.ui.main.home

import com.day.palette.common.presentation.utils.UiText


sealed class HomeSideEffect {
    /**Actions that can be performed on the UI*/
    data class ShowToast(val message: UiText) : HomeSideEffect()
    data class ShowSnack(val message: UiText) : HomeSideEffect()

    /**Actions that can be used to invoke functions on the ViewModel*/
    data object GetCountryHolidays : HomeSideEffect()
}