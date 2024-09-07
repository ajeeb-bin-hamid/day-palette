package com.day.palette.common.presentation.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.day.palette.R
import com.day.palette.common.domain.utils.Errors.Network
import com.day.palette.common.domain.utils.Errors.Prefs
import com.day.palette.common.domain.utils.GenericError
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import org.orbitmvi.orbit.ContainerHost
import java.text.SimpleDateFormat
import java.util.Locale

/**This extension converts all GenericErrors into their corresponding string literals.*/
fun GenericError.asUiText(): UiText {
    return when (this) {
        //Errors that can occur while performing network calls
        Network.NO_RESPONSE -> UiText.StringResource(R.string.error_no_response)
        Network.NO_INTERNET -> UiText.StringResource(R.string.error_no_internet)
        Network.REQUEST_TIMEOUT -> UiText.StringResource(R.string.error_request_timeout)
        Network.INTERNAL_ERROR -> UiText.StringResource(R.string.error_internal_error)
        Network.PAYLOAD_TOO_LARGE -> UiText.StringResource(R.string.error_payload_too_large)
        Network.UNKNOWN -> UiText.StringResource(R.string.error_unknown)

        //Errors that can occur while performing operations on SharedPreferences
        Prefs.NO_SUCH_DATA -> UiText.StringResource(R.string.error_not_data_found)
        Prefs.UNKNOWN -> UiText.StringResource(R.string.error_unknown)
    }
}

/**This extension converts integer values into the corresponding DP (density-independent pixels) values needed for UI elements.*/
inline val Int.DP: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
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

/**This extension returns a new DiffUtil.Callback initialized with the provided arguments,
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

/**This extension parses a given date in the yyyy-MM-dd format and
 * returns it formatted according to the specified expression.*/
fun String.getFormattedDate(exp: String): String {
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(this) ?: return ""
    return SimpleDateFormat(exp, Locale.US).format(date)
}

/**This extension simplifies displaying Toast messages by eliminating repetitive boilerplate code.*/
fun Context.showToast(uiText: UiText, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, uiText.asString(this), duration).show()
}

/**This extension allows to display a Snackbar without redundant boilerplate code.*/
fun Context.showSnack(view: View, uiText: UiText, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, uiText.asString(this), duration).show()
}

/**This extension helps retrieve colors based on the app's applied theme.*/
fun Context.getThemeColor(id: Int): Int {
    val maskTypedValue = TypedValue()
    theme.resolveAttribute(id, maskTypedValue, true)
    return ContextCompat.getColor(this, maskTypedValue.resourceId)
}

/**This extension streamlines access to read-only data from the ViewModel's state.*/
val <STATE : Any, SIDE_EFFECT : Any> ContainerHost<STATE, SIDE_EFFECT>.currentState: STATE
    get() = container.stateFlow.value

/**This extension simplifies implementing item click listeners in RecyclerView, reducing boilerplate code.*/
fun <T> LivelyAdapter<T>.setOnItemClickListener(listener: OnItemClickListener<T>) {
    this.onItemClickListener = listener
}

/**This extension simplifies setting up long press listeners for RecyclerView items, reducing boilerplate code.*/
fun <T> LivelyAdapter<T>.setOnItemLongPressListener(listener: OnItemLongPressListener<T>) {
    this.onItemLongPressListener = listener
}

/**This extension simplifies the implementation of a double-tap listener for RecyclerView items, reducing boilerplate code.*/
fun <T> LivelyAdapter<T>.setOnItemDoubleTapListener(listener: OnItemDoubleTapListener<T>) {
    this.onItemDoubleTapListener = listener
}

/**This extension simplifies the initialization of a BottomSheetFragment with arguments,
 * eliminating boilerplate code and improving code clarity.*/
fun FragmentManager.showBottomSheet(
    fragment: BottomSheetDialogFragment, arguments: Bundle.() -> Unit = {}
) {
    fragment.arguments = Bundle().apply(arguments)
    fragment.show(this, fragment.javaClass.simpleName)
}
