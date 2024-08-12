package com.day.palette.presentation.ui.main.home

import android.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.palette.domain.GenericResult
import com.day.palette.domain.model.SelectedCountryDetails
import com.day.palette.domain.usecase.GetAllCountriesUseCase
import com.day.palette.domain.usecase.GetCountryHolidaysUseCase
import com.day.palette.domain.usecase.GetSelectedCountryDetailsUseCase
import com.day.palette.domain.usecase.SetSelectedCountryDetailsUseCase
import com.day.palette.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getSelectedCountryDetailsUseCase: GetSelectedCountryDetailsUseCase,
    private val getCountryHolidaysUseCase: GetCountryHolidaysUseCase,
    private val getAllCountriesUseCase: GetAllCountriesUseCase,
    private val setSelectedCountryDetailsUseCase: SetSelectedCountryDetailsUseCase
) : ViewModel(), ContainerHost<HomeState, HomeIntent> {

    private val initialState = HomeState(
        selectedCountryName = DEFAULT_COUNTRY_NAME,
        selectedCountryCode = DEFAULT_COUNTRY_CODE,
        countryHolidays = emptyList(),
        allCountries = emptyList(),
        isLoading = false
    )

    override val container = container<HomeState, HomeIntent>(initialState, savedStateHandle)

    /**This block evaluates the shared preference use case call.
     * If the call is successful, the state is updated with the retrieved data.*/
    init {
        when (val selectedCountryDetails = getSelectedCountryDetailsUseCase.execute()) {
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

    /**Public function exposed to UI components such as Activities, Fragments & Bottom sheets,
     * allowing them to invoke operations on this ViewModel.*/
    fun invoke(action: HomeIntent) = intent {
        when (action) {
            is HomeIntent.GetCountryHolidays -> {
                getCountryHolidays(state.selectedCountryCode)
            }

            is HomeIntent.GetAllCountries -> {
                getAllCountries()
            }

            is HomeIntent.SetSelectedCountry -> {
                setSelectedCountry(action.selectedCountryDetails)
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
                    countryHolidays.data.forEach { item -> item.bgColor = getRandomDarkColor() }
                    intent { reduce { state.copy(countryHolidays = countryHolidays.data) } }
                }

                is GenericResult.Error -> {
                    intent { postSideEffect(HomeIntent.ShowSnack(countryHolidays.error.asUiText())) }
                }
            }

        }
    }

    private fun getAllCountries() {
        viewModelScope.launch {
            when (val allCountries = getAllCountriesUseCase.execute()) {
                is GenericResult.Success -> {
                    allCountries.data.find { it.code == container.stateFlow.value.selectedCountryCode }?.isSelected =
                        true
                    intent { reduce { state.copy(allCountries = allCountries.data) } }
                }

                is GenericResult.Error -> {
                    intent { postSideEffect(HomeIntent.ShowToast(allCountries.error.asUiText())) }
                }
            }
        }
    }

    private fun setSelectedCountry(selectedCountryDetails: SelectedCountryDetails) {
        when (val setSelectedCountryCall =
            setSelectedCountryDetailsUseCase.execute(selectedCountryDetails)) {
            is GenericResult.Success -> {
                intent {
                    reduce {
                        state.copy(
                            selectedCountryName = selectedCountryDetails.selectedCountryName,
                            selectedCountryCode = selectedCountryDetails.selectedCountryCode
                        )
                    }
                    postSideEffect(HomeIntent.GetCountryHolidays)
                }
            }

            is GenericResult.Error -> {
                intent { postSideEffect(HomeIntent.ShowSnack(setSelectedCountryCall.error.asUiText())) }
            }
        }
    }

    private fun getRandomDarkColor(): Int {
        var color: Int
        // Generate a random color
        do {
            color = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        } while (isColorBright(color))
        return color
    }

    private fun isColorBright(color: Int): Boolean {
        val r = Color.red(color) / 255.0
        val g = Color.green(color) / 255.0
        val b = Color.blue(color) / 255.0

        // Calculate luminance
        val luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b
        return luminance > 0.5 // Bright color threshold
    }

    companion object {
        const val DEFAULT_COUNTRY_NAME = "United States"
        const val DEFAULT_COUNTRY_CODE = "US"
    }
}