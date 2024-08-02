package com.day.palette.main.home

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.day.palette.R
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then


class HomeDefs {

    @Given("Iam in the home screen")
    fun iAmInHomeScreen() {
        Espresso.onView(withId(R.id.main)).check(
            matches(
                isDisplayed()
            )
        )
    }

    @Then("I can see text in home screen")
    fun iCanSeeText() {
        Espresso.onView(withId(R.id.mainActivityTV)).check(
            matches(
                isDisplayed()
            )
        )
    }
}