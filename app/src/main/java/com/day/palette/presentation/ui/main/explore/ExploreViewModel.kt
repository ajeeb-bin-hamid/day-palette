package com.day.palette.presentation.ui.main.explore

import android.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.day.palette.domain.usecase.GetWorldWideHolidaysUseCase
import com.day.palette.domain.utils.GenericResult
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
class ExploreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getWorldWideHolidaysUseCase: GetWorldWideHolidaysUseCase
) : ViewModel(), ContainerHost<ExploreState, ExploreSideEffect> {

    private val initialState = ExploreState(holidays = ArrayList())

    override val container =
        container<ExploreState, ExploreSideEffect>(initialState, savedStateHandle)

    /**Public function exposed to UI components such as Activities, Fragments, and Bottom Sheets,
     * allowing them to perform operations on this ViewModel.
     * Do not call these functions or any functions declared within them directly from the ViewModel.
     * Instead, use side effects to invoke these functions from the UI components.*/
    fun invoke(action: ExploreIntent) = intent {
        when (action) {
            is ExploreIntent.GetWorldWideHolidays -> getWorldWideHolidays()
        }
    }

    private fun getWorldWideHolidays() {
        viewModelScope.launch {
            when (val holidaysCall = getWorldWideHolidaysUseCase.execute()) {
                is GenericResult.Success -> {
                    holidaysCall.data.forEach { item -> item.bgColor = getRandomDarkColor() }
                    intent { reduce { state.copy(holidays = holidaysCall.data) } }
                }

                is GenericResult.Error -> {
                    intent { postSideEffect(ExploreSideEffect.ShowSnack(holidaysCall.error.asUiText())) }
                }
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
}