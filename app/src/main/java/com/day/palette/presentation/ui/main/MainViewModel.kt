package com.day.palette.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(), ContainerHost<MainState, Nothing> {

    override val container: Container<MainState, Nothing> = container(MainState())

    /**uiActions is the SharedFlow used to perform any actions on the UI*/
    private val _uiActions = MutableSharedFlow<MainIntent>()
    val uiActions: SharedFlow<MainIntent> = _uiActions


    /**This function is used by UI component to invoke any actions on the ViewModel*/
    fun vmActions(vmAction: MainIntent) {
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
        makeUIChange()
    }

    private fun makeUIChange() {
        viewModelScope.launch {
            _uiActions.emit(MainIntent.ShowToast("hello" + container.stateFlow.value.isLoading))
        }
    }


    private suspend fun fetchData() {
        //

    }

}