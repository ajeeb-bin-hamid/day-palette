package com.day.palette.presentation.ui.main

sealed class MainIntent {
    /**Actions that can be invoked on the ViewModel*/
    data object FetchData : MainIntent()

    /**Actions that can be performed on the UI*/
    data class ShowToast(val message: String) : MainIntent()
    data class ShowSnack(val message: String) : MainIntent()
}
