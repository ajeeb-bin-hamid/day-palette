package com.day.palette.data.utils

import androidx.test.espresso.IdlingResource

class AppIdlingResource : IdlingResource {

    @Volatile
    private var callback: IdlingResource.ResourceCallback? = null
    private val backgroundOperations = mutableListOf<() -> Unit>()

    override fun getName(): String = "AppIdlingResource"

    override fun isIdleNow(): Boolean = backgroundOperations.isEmpty()

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }

    fun registerBackgroundOperation(operation: () -> Unit) {
        backgroundOperations.add(operation)
        checkIdleState()
    }

    fun unregisterBackgroundOperation(operation: () -> Unit) {
        backgroundOperations.remove(operation)
        checkIdleState()
    }

    private fun checkIdleState() {
        if (backgroundOperations.isEmpty()) {
            callback?.onTransitionToIdle()
        }
    }
}
