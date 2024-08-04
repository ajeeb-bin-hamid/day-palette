package com.day.palette.presentation.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.palette.GenericResult
import com.day.palette.domain.usecase.GetCountryHolidaysUseCase
import com.day.palette.domain.usecase.GetSelectedCountryDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getSelectedCountryDetailsUseCase: GetSelectedCountryDetailsUseCase,
    private val getCountryHolidaysUseCase: GetCountryHolidaysUseCase
) : ViewModel(), ContainerHost<HomeState, HomeIntent> {

    private val selectedCountryDetails = getSelectedCountryDetailsUseCase.execute()

    private val initialState = HomeState(
        selectedCountryName = selectedCountryDetails.selectedCountryName,
        selectedCountryCode = selectedCountryDetails.selectedCountryCode,
        countryHolidays = emptyList(),
        isLoading = false
    )

    override val container: Container<HomeState, HomeIntent> = container(initialState)

    fun invoke(action: HomeIntent) = intent {
        when (action) {
            HomeIntent.GetCountryHolidays -> {
                getCountryHolidays(selectedCountryDetails.selectedCountryCode)
            }

            else -> {
                //
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
                    countryHolidays.e.message?.let { intent { postSideEffect(HomeIntent.ShowSnack(it)) } }
                }
            }

        }
    }
}