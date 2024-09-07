package com.day.palette.steps.main

import androidx.core.view.isVisible
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.day.palette.R
import com.day.palette.home.presentation.ui.main.home.HomeRecyclerAdapter
import com.day.palette.utils.validateText
import com.day.palette.utils.validationGroup
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then


class HomeSteps {

    @Given("I am on the home screen")
    fun iAmInHomeScreen() {
        Espresso.onView(withId(R.id.mainActivity)).check(matches(isDisplayed()))
    }

    @Then("I should see the title displayed at the top of the home screen")
    fun iShouldSeeTitleAtTop() {
        Espresso.onView(withId(R.id.homeFragmentTitleTV)).check(matches(isDisplayed()))
    }

    @And("I should see the list of future holidays on the home screen")
    fun iShouldSeeListOfHolidays() {
        Espresso.onView(withId(R.id.homeFragmentRV)).check(matches(isDisplayed()))
    }

    @And("Each holiday on the home screen should display date and name")
    fun eachHolidayShouldDisplayData() {

        val validateDayText = validateText(R.id.cardHolidayCompactDayTV) {
            require(isVisible) //Must be visible
            require(length() == 2) //Text must be of length 2
            require(text.all { it.isDigit() }) //All chars must be digits
        }

        val validateMonthText = validateText(R.id.cardHolidayCompactMonthTV) {
            require(isVisible) //Must be visible
            require(text.contains(Regex("""^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec), \d{4}$"""))) //Must follow given regex
        }

        val validateNameText = validateText(R.id.cardHolidayCompactImportanceTV) {
            require(isVisible) //Must be visible
        }

        Espresso.onView(withId(R.id.homeFragmentRV)).perform(
            RecyclerViewActions.actionOnItemAtPosition<HomeRecyclerAdapter.HomeRecyclerViewHolder>(
                0, validationGroup(validateDayText, validateMonthText, validateNameText)
            )
        )
    }
}