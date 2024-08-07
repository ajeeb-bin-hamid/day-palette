package com.day.palette.presentation.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.palette.domain.GenericResult
import com.day.palette.domain.usecase.GetCountryHolidaysUseCase
import com.day.palette.domain.usecase.GetSelectedCountryDetailsUseCase
import com.day.palette.presentation.asUiText
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
        selectedCountryName = DEFAULT_COUNTRY_NAME,
        selectedCountryCode = DEFAULT_COUNTRY_CODE,
        countryHolidays = emptyList(),
        isLoading = false
    )

    override val container: Container<HomeState, HomeIntent> = container(initialState)

    /**This block evaluates the shared preference use case call.
     * If the call is successful, the state is updated with the retrieved data.*/
    init {
        when (selectedCountryDetails) {
            is GenericResult.Success -> {
                intent {
                    reduce {
                        state.copy(
                            selectedCountryName = selectedCountryDetails.data.selectedCountryName,
                            selectedCountryCode = selectedCountryDetails.data.selectedCountryCode
                        )
                    }
                }
            }

            is GenericResult.Error -> {
                //
            }
        }
    }

    /**Public function exposed to UI components such as Activities and Fragments,
     * allowing them to invoke operations on this ViewModel.*/
    fun invoke(action: HomeIntent) = intent {
        when (action) {
            HomeIntent.GetCountryHolidays -> {
                getCountryHolidays(state.selectedCountryCode)
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
                    intent { reduce { state.copy(countryHolidays = countryHolidays.data) } }
                }

                is GenericResult.Error -> {
                    intent { postSideEffect(HomeIntent.ShowSnack(countryHolidays.error.asUiText())) }
                }
            }

        }
    }

    companion object {
        const val DEFAULT_COUNTRY_NAME = "United States"
        const val DEFAULT_COUNTRY_CODE = "US"
    }
}