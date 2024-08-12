package com.day.palette.presentation.utils

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

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

/**This extension simplifies the implementation of custom RecyclerView item decorations,
 * eliminating boilerplate code for a cleaner and more efficient setup*/
inline fun RecyclerView.itemDecoration(
    margin: Int, crossinline configureOffsets: (outRect: Rect, position: Int, margin: Int) -> Unit
) {
    val decoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            configureOffsets(outRect, position, margin)
        }
    }
    addItemDecoration(decoration)
}