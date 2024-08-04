package com.day.palette.presentation.ui.main.home

sealed class HomeIntent {
    /**vmActions - Actions can be invoked on the ViewModel*/
    data object GetCountryHolidays : HomeIntent()

    /**uiActions - Actions can be performed on the UI*/
    data class ShowSnack(val message: String) : HomeIntent()
}