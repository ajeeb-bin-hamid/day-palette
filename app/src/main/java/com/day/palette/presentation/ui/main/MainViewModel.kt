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
) : ViewModel(), ContainerHost<MainState, MainIntent> {

    override val container = container<MainState, MainIntent>(MainState(), savedStateHandle)


    /**Public function exposed to UI components such as Activities, Fragments & Bottom sheets,
     * allowing them to invoke operations on this ViewModel.*/
    fun invoke(action: MainIntent) = intent {
        //
    }

}