package com.day.palette.presentation.utils

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue

/**This extension converts integer values into the corresponding DP (density-independent pixels) values needed for UI elements.*/
fun Context.toPx(dp: Int): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics
).toInt()

/**This extension leverages the latest methods for handling Parcelable data on newer Android versions
 * while also providing backward compatibility for older versions using deprecated methods.*/
inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}