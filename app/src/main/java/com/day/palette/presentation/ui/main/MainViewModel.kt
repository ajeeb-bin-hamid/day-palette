package com.day.palette.presentation.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<MainState, MainSideEffect> {

    override val container = container<MainState, MainSideEffect>(MainState(), savedStateHandle)


    /**Public function exposed to UI components such as Activities, Fragments, and Bottom Sheets,
     * allowing them to perform operations on this ViewModel.
     * Do not call these functions or any functions declared within them directly from the ViewModel.
     * Instead, use side effects to invoke these functions from the UI components.*/
    fun invoke(action: MainIntent) = intent {
        //
    }

}