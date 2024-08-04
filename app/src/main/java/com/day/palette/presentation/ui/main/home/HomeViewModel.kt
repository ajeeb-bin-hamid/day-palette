package com.day.palette.presentation.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.palette.domain.usecase.GetCountryHolidaysUseCase
import com.day.palette.domain.usecase.GetSelectedCountryDetailsUseCase
import com.day.palette.GenericResult
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
class HomeViewModel @Inject constructor(
    private val getSelectedCountryDetailsUseCase: GetSelectedCountryDetailsUseCase,
    private val getCountryHolidaysUseCase: GetCountryHolidaysUseCase
) : ViewModel(), ContainerHost<HomeState, Nothing> {

    private val selectedCountryDetails = getSelectedCountryDetailsUseCase.execute()

    private val initialState = HomeState(
        selectedCountryName = selectedCountryDetails.selectedCountryCode,
        selectedCountryCode = selectedCountryDetails.selectedCountryCode,
        countryHolidays = emptyList(),
        isLoading = false
    )
    override val container: Container<HomeState, Nothing> = container(initialState)

    /**uiActions is the SharedFlow used to perform any actions on the UI*/
    private val _uiActions = MutableSharedFlow<HomeIntent>()
    val uiActions: SharedFlow<HomeIntent> = _uiActions


    fun vmActions(action: HomeIntent) {
        intent {
            when (action) {
                HomeIntent.GetCountryHolidays -> {
                    getCountryHolidays(selectedCountryDetails.selectedCountryCode)
                }

                else -> {
                    //
                }
            }
        }
    }

    private fun getCountryHolidays(countryCode: String) {
        viewModelScope.launch {
            when (val countryHolidays = getCountryHolidaysUseCase.execute(countryCode)) {
                is GenericResult.Success -> {
                    countryHolidays.data?.let { intent { reduce { state.copy(countryHolidays = it) } } }
                }

                is GenericResult.Error -> {
                    countryHolidays.e.message?.let { _uiActions.emit(HomeIntent.ShowSnack(it)) }
                }
            }

        }
    }
}