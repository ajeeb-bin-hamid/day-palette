package com.day.palette

import android.app.Activity
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.day.palette.presentation.ui.main.MainActivity
import io.cucumber.java.After
import io.cucumber.java.Before

class CucumberScenarioHolder {

    private var scenario: ActivityScenario<*>? = null

    @Before
    fun setUp() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val intent = Intent(instrumentation.targetContext, MainActivity::class.java)
        scenario = ActivityScenario.launch<Activity>(intent).onActivity { activity ->
            instrumentation.uiAutomation.apply {
                //Declare the necessary permissions here
            }
        }

    }

    @After
    fun tearDown() {
        scenario?.close()
    }
}