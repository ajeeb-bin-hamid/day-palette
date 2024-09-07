package com.day.palette.home.presentation.ui.holiday

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.day.palette.R
import com.day.palette.databinding.ActivityHolidayBinding
import com.day.palette.home.domain.model.Holiday
import com.day.palette.common.presentation.utils.INSTANCE_POPULATED
import com.day.palette.common.presentation.utils.TypefaceSpan
import com.day.palette.common.presentation.utils.getFormattedDate
import com.day.palette.common.presentation.utils.showSnack
import com.day.palette.common.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.viewmodel.observe

@AndroidEntryPoint
class HolidayActivity : AppCompatActivity() {

    private lateinit var b: ActivityHolidayBinding
    private val vm: HolidayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        b = ActivityHolidayBinding.inflate(layoutInflater)
        setContentView(b.root)
        vm.observe(this, state = ::observeState, sideEffect = ::observeSideEffect)

        // Perform all the UI setup here
        setUpInsets()
        setUpParentLayout()

        // Check if UI component is recreating itself
        if (savedInstanceState != null) {
            // Restore from savedInstanceState
        } else {
            //
        }
    }

    /** Observe changes in the State using Orbit StateFlow */
    private fun observeState(state: HolidayState) {
        modifyHoliday(state.holiday)
    }

    /** Observe side effects using Orbit StateFlow */
    private fun observeSideEffect(action: HolidaySideEffect) {
        when (action) {
            is HolidaySideEffect.ShowToast -> this.showToast(action.message)
            is HolidaySideEffect.ShowSnack -> this.showSnack(b.root, action.message)
        }
    }

    private fun setUpInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(b.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    private fun setUpParentLayout() {
        val parentTransitionName = intent.getStringExtra(PARENT_TRANSITION_NAME)
        parentTransitionName?.let { b.root.transitionName = it }

        val dayTextTransitionName = intent.getStringExtra(DAY_TEXT_TRANSITION_NAME)
        dayTextTransitionName?.let { b.holidayActivityDayTV.transitionName = it }
    }

    private fun modifyHoliday(holiday: Holiday?) {
        holiday?.let {
            b.holidayActivity.setBackgroundColor(holiday.bgColor)
            b.holidayActivityDayTV.text = holiday.date.getFormattedDate("dd")
            b.holidayActivityMonthTV.text = holiday.date.getFormattedDate("MMMM, yyyy")
            b.holidayActivityTypeTV.text =
                if (holiday.global) getString(R.string.global) else getString(R.string.regional)
            b.holidayActivityImportanceTV.text = holiday.name

            //Create description
            val type =
                if (holiday.types.contains("Public")) getString(R.string.a) else getString(R.string.not_a)
            val observed =
                if (holiday.global) getString(R.string.globally) else getString(R.string.locally)
            val holidayDescription = getString(
                R.string.holiday_description,
                holiday.date,
                holiday.name,
                holiday.localName,
                holiday.countryCode,
                type,
                observed
            )

            val boldFont: Typeface? = ResourcesCompat.getFont(this, R.font.poppins_semi_bold_italic)
            val spannableString = SpannableString(holidayDescription)
            val spanStart = 33 + holiday.date.length + holiday.name.length
            val spanEnd = spanStart + holiday.localName.length

            // Apply bold font to a specific portion
            boldFont?.let {
                val customTypefaceSpan = TypefaceSpan("", it)
                spannableString.setSpan(
                    customTypefaceSpan, spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            b.holidayActivityDescriptionTV.text = spannableString

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(INSTANCE_POPULATED, true)
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val PARENT_TRANSITION_NAME = "parent_transition_name"
        const val DAY_TEXT_TRANSITION_NAME = "day_text_transition_name"
        const val TYPE_TEXT_TRANSITION_NAME = "type_text_transition_name"
        const val MONTH_TEXT_TRANSITION_NAME = "month_text_transition_name"
        const val IMPORTANCE_TEXT_TRANSITION_NAME = "importance_text_transition_name"
    }
}
