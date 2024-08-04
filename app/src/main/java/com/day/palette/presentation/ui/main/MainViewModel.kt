package com.day.palette.presentation.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(), ContainerHost<MainState, MainIntent> {

    override val container: Container<MainState, MainIntent> = container(MainState())


    /**This function is used by UI component to invoke any actions on the ViewModel*/
    fun invoke(vmAction: MainIntent) = intent {
        when (vmAction) {
            MainIntent.FetchData -> {
                dos()
            }

            else -> {
                //
            }
        }
    }

    private fun dos() = intent {
        reduce { state.copy(isLoading = true) }
    }


}