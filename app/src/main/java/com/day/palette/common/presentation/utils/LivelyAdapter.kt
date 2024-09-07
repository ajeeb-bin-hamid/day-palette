package com.day.palette.common.presentation.utils

import android.view.View
import androidx.core.util.Pair

// Functional interface with vararg for shared elements
fun interface OnItemClickListener<T> {
    fun onItemClick(position: Int, item: T, type: String, vararg sharedElements: Pair<View, String>)
}

fun interface OnItemLongPressListener<T> {
    fun onItemLongPress(
        position: Int, item: T, type: String, vararg sharedElements: Pair<View, String>
    )
}

 fun interface OnItemDoubleTapListener<T> {
    fun onItemDoubleTap(
        position: Int, item: T, type: String, vararg sharedElements: Pair<View, String>
    )
}

interface LivelyAdapter<T> {
    var onItemClickListener: OnItemClickListener<T>?
    var onItemLongPressListener: OnItemLongPressListener<T>?
    var onItemDoubleTapListener: OnItemDoubleTapListener<T>?
}