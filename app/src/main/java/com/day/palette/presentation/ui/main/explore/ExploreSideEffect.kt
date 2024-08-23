package com.day.palette.presentation.ui.main.explore

import com.day.palette.presentation.utils.UiText

sealed class ExploreSideEffect {
    /**Actions that can be performed on the UI*/
    data class ShowToast(val message: UiText) : ExploreSideEffect()
    data class ShowSnack(val message: UiText) : ExploreSideEffect()

    /**Actions that can be used to invoke functions on the ViewModel*/
}