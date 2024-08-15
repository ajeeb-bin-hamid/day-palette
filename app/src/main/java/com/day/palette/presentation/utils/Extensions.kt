package com.day.palette.presentation.utils

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.DiffUtil
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

/**This extension streamlines the implementation of TextChangeListener for TextView elements,
 * eliminating unnecessary boilerplate code.*/
fun EditText.onTextChanged(
    afterTextChanged: (String) -> Unit,
    beforeTextChanged: (CharSequence, Int, Int, Int) -> Unit = { _, _, _, _ -> },
    onTextChanged: (CharSequence, Int, Int, Int) -> Unit = { _, _, _, _ -> }
) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged(s ?: "", start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s ?: "", start, before, count)
        }
    })
}

/**his extension returns a new DiffUtil.Callback initialized with the provided arguments,
 * making it easier to update items in a RecyclerView.*/
fun <T> diffCallback(
    oldList: List<T>,
    newList: List<T>,
    areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    areContentsTheSame: (oldItem: T, newItem: T) -> Boolean = { oldItem, newItem -> oldItem == newItem }
): DiffUtil.Callback = object : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
}
