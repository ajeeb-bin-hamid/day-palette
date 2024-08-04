package com.day.palette.presentation.ui.main.home

sealed class HomeIntent {
    /**Actions that can be invoked on the ViewModel*/
    data object GetCountryHolidays : HomeIntent()

    /**Actions that can be performed on the UI*/
    data class ShowToast(val message: String) : HomeIntent()
    data class ShowSnack(val message: String) : HomeIntent()
}