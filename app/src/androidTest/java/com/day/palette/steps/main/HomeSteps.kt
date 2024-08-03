package com.day.palette.steps.main

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.day.palette.R
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then


class HomeSteps {

    @Given("I am on the home screen")
    fun iAmInHomeScreen() {
        Espresso.onView(withId(R.id.main)).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Then("I should see the text {string} on the home screen")
    fun iCanSeeText(text: String) {
        Espresso.onView(withText(text)).check(
            matches(
                isDisplayed()
            )
        )
    }
}