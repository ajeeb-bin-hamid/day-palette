package com.day.palette.utils

import android.view.View
import android.widget.TextView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher

/**This extension takes multiple ViewActions as input, executes each one sequentially,
 * and returns the result as a single ViewAction.
 * This is useful in scenarios where multiple view actions need to be performed,
 * but only one action is allowed as an argument.*/
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

/**This extension helps check test conditions on TextViews and eliminates boilerplate code.*/
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
