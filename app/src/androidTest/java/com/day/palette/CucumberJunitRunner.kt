package com.day.palette

import android.os.Bundle
import io.cucumber.android.runner.CucumberAndroidJUnitRunner
import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith
import java.io.File

/**When running BDD tests in an automated pipeline, it's crucial to store the resulting test reports.
 * These reports are generated in the app's data directory within the Android folder.
 * The reports, available in both XML and HTML formats,
 * can be retrieved from this location and stored in a more permanent and accessible location after the tests have completed.
 * This ensures that the results are preserved and can be reviewed or analyzed as needed.
 *
 * Example reports location if we are running BDD tests in the 'dev' environment:
 * /storage/emulated/0/Android/data/com.day.palette.dev/files/reports/cucumber.html
 * /storage/emulated/0/Android/data/com.day.palette.dev/files/reports/cucumber.xml*/

@RunWith(Cucumber::class)
@CucumberOptions
class CucumberJunitRunner : CucumberAndroidJUnitRunner() {
    override fun onCreate(bundle: Bundle) {
        bundle.putString(
            "plugin", pluginConfigurationString
        )

        File(absoluteFilesPath).mkdirs()
        super.onCreate(bundle)
    }

    private val pluginConfigurationString: String
        get() {
            val cucumber = "cucumber"
            val separator = "--"
            return "junit:" + cucumber.getCucumberXml() + separator + "html:" + cucumber.getCucumberHtml()
        }

    private fun String.getCucumberHtml(): String {
        return "$absoluteFilesPath/$this.html"
    }

    private fun String.getCucumberXml(): String {
        return "$absoluteFilesPath/$this.xml"
    }

    private val absoluteFilesPath: String
        get() {
            val directory = targetContext.getExternalFilesDir(null)
            return File(directory, "reports").absolutePath
        }
}
