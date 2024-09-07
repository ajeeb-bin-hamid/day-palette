package com.day.palette.home.presentation.ui.holiday

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.day.palette.common.presentation.utils.VIEW_MODEL_STATE
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HolidayViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<HolidayState, HolidaySideEffect> {

    private val initialState = savedStateHandle[VIEW_MODEL_STATE] ?: HolidayState()

    override val container =
        container<HolidayState, HolidaySideEffect>(initialState, savedStateHandle)

    /** Public function exposed to UI components such as Activities, Fragments, and Bottom Sheets,
     * allowing them to perform operations on this ViewModel.
     * Do not call these functions or any functions declared within them directly from the ViewModel.
     * Instead, use side effects to invoke these functions from the UI components. */
    fun invoke(action: HolidayIntent) = intent {
        //
    }
}
