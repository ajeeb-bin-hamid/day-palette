package com.day.palette.home.presentation.ui.holiday

import com.day.palette.common.presentation.utils.UiText

sealed class HolidaySideEffect {
    /** Actions that can be performed on the UI */
    data class ShowToast(val message: UiText) : HolidaySideEffect()
    data class ShowSnack(val message: UiText) : HolidaySideEffect()

    /** Actions that can be used to invoke functions on the ViewModel */
}
