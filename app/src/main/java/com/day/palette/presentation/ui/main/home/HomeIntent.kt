package com.day.palette.presentation.ui.main.home

import com.day.palette.presentation.UiText

sealed class HomeIntent {
    /**Actions that can be invoked on the ViewModel*/
    data object GetCountryHolidays : HomeIntent()

    /**Actions that can be performed on the UI*/
    data class ShowToast(val message: UiText) : HomeIntent()
    data class ShowSnack(val message: UiText) : HomeIntent()
}