package com.day.palette.utils

import android.view.View
import android.widget.TextView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher

fun validationGroup(vararg actions: ViewAction): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Group of validations"
        }

        override fun perform(uiController: UiController, view: View) {
            actions.forEach { action ->
                action.perform(uiController, view)
            }
        }
    }
}

fun validateText(id: Int, action: TextView.() -> Unit): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Validate text with ID $id"
        }

        override fun perform(uiController: UiController, view: View) {
            val textView = view.findViewById<TextView>(id)
            action(textView)
        }
    }
}
