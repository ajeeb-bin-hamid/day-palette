package com.day.palette.home.presentation.ui.main.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.day.palette.databinding.CardHolidayCompactBinding
import com.day.palette.home.domain.model.Holiday
import com.day.palette.home.presentation.ui.holiday.HolidayActivity.Companion.DAY_TEXT_TRANSITION_NAME
import com.day.palette.home.presentation.ui.holiday.HolidayActivity.Companion.PARENT_TRANSITION_NAME
import com.day.palette.common.presentation.utils.LivelyAdapter
import com.day.palette.common.presentation.utils.OnItemClickListener
import com.day.palette.common.presentation.utils.OnItemDoubleTapListener
import com.day.palette.common.presentation.utils.OnItemLongPressListener
import com.day.palette.common.presentation.utils.RECYCLER_ITEM_PARENT
import com.day.palette.common.presentation.utils.diffCallback
import com.day.palette.common.presentation.utils.getFormattedDate

class HomeRecyclerAdapter(
    private val context: Context, private var countryHolidays: ArrayList<Holiday>
) : RecyclerView.Adapter<HomeRecyclerAdapter.HomeRecyclerViewHolder>(), LivelyAdapter<Holiday> {

    override var onItemClickListener: OnItemClickListener<Holiday>? = null
    override var onItemLongPressListener: OnItemLongPressListener<Holiday>? = null
    override var onItemDoubleTapListener: OnItemDoubleTapListener<Holiday>? = null

    class HomeRecyclerViewHolder(b: CardHolidayCompactBinding) : RecyclerView.ViewHolder(b.root) {
        val cardHolidayCompact = b.cardHolidayCompact
        val cardHolidayCompactBgCL = b.cardHolidayCompactBgCL
        val cardHolidayCompactDayTV = b.cardHolidayCompactDayTV
        val cardHolidayCompactMonthTV = b.cardHolidayCompactMonthTV
        val cardHolidayCompactImportanceTV = b.cardHolidayCompactImportanceTV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerViewHolder {
        val b = CardHolidayCompactBinding.inflate(
            LayoutInflater.from(context), parent, false
        )

        return HomeRecyclerViewHolder(b)
    }

    override fun getItemCount(): Int {
        return countryHolidays.size
    }

    override fun onBindViewHolder(h: HomeRecyclerViewHolder, position: Int) {
        val holiday = countryHolidays[position]

        h.cardHolidayCompactBgCL.setBackgroundColor(holiday.bgColor)
        h.cardHolidayCompactDayTV.text = holiday.date.getFormattedDate("dd")
        h.cardHolidayCompactMonthTV.text = holiday.date.getFormattedDate("MMM, yyyy")
        h.cardHolidayCompactImportanceTV.text = holiday.name

        h.cardHolidayCompact.transitionName = PARENT_TRANSITION_NAME + position
        h.cardHolidayCompactDayTV.transitionName = DAY_TEXT_TRANSITION_NAME + position

        h.cardHolidayCompact.setOnClickListener {
            onItemClickListener?.onItemClick(
                position = position,
                item = holiday,
                type = RECYCLER_ITEM_PARENT,
                Pair(h.cardHolidayCompact, h.cardHolidayCompact.transitionName),
                Pair(h.cardHolidayCompactDayTV, h.cardHolidayCompactDayTV.transitionName)
            )
        }


    }

    fun updateItems(newItems: ArrayList<Holiday>) {
        val diffCallback = diffCallback(
            oldList = countryHolidays,
            newList = newItems,
            areItemsTheSame = { oldItem, newItem ->
                "${oldItem.countryCode}${oldItem.bgColor}" == "${newItem.countryCode}${newItem.bgColor}"
            })

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        countryHolidays = newItems
        diffResult.dispatchUpdatesTo(this)
    }

}