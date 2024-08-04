package com.day.palette.presentation.ui.main.home

import androidx.lifecycle.ViewModel
import com.day.palette.domain.usecase.GetSelectedCountryDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getSelectedCountryDetailsUseCase: GetSelectedCountryDetailsUseCase
) : ViewModel(), ContainerHost<HomeState, Nothing> {

    private val selectedCountryDetails = getSelectedCountryDetailsUseCase.execute()

    private val initialState = HomeState(
        selectedCountryName = selectedCountryDetails.selectedCountryCode,
        selectedCountryCode = selectedCountryDetails.selectedCountryCode
    )
    override val container: Container<HomeState, Nothing> = container(initialState)

    /**uiActions is the SharedFlow used to perform any actions on the UI*/
    private val _uiActions = MutableSharedFlow<HomeIntent>()
    val uiActions: SharedFlow<HomeIntent> = _uiActions

    fun vmActions(action: HomeIntent) {
        when (action) {

            else -> {
                //
            }
        }
    }
}