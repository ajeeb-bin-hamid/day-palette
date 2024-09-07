package com.day.palette

import android.app.Activity
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import com.day.palette.common.data.utils.AppIdlingResource
import com.day.palette.home.presentation.ui.main.MainActivity
import io.cucumber.java.After
import io.cucumber.java.Before
import javax.inject.Inject

class CucumberScenarioHolder @Inject constructor(private val appIdlingResource: AppIdlingResource) {

    private var scenario: ActivityScenario<*>? = null

    /**This function will execute before each scenario,
     * allowing for any initial setup, including granting necessary app permissions.*/
    @Before
    fun setUp() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val intent = Intent(instrumentation.targetContext, MainActivity::class.java)
        scenario = ActivityScenario.launch<Activity>(intent).onActivity { activity ->
            instrumentation.uiAutomation.apply {
                //Declare the necessary permissions here
            }
        }

        //Register the IdlingResource
        IdlingRegistry.getInstance().register(appIdlingResource)

    }

    /**This function will be executed after each scenario, during which we will close the Scenario Holder.*/
    @After
    fun tearDown() {
        scenario?.close()

        //Unregister the IdlingResource
        IdlingRegistry.getInstance().unregister(appIdlingResource)
    }
}