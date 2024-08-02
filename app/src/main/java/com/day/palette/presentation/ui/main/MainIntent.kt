package com.day.palette.presentation.ui.main

sealed class MainIntent {
    /**vmActions - Actions can be invoked on the ViewModel*/
    data object FetchData : MainIntent()

    /**uiActions - Actions can be performed on the UI*/
    data class ShowToast(val message: String) : MainIntent()
}
