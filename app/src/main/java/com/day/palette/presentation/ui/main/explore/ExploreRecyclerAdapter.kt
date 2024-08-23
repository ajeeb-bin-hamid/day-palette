package com.day.palette.presentation.ui.main.explore

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.day.palette.R
import com.day.palette.databinding.CardHolidayBinding
import com.day.palette.domain.model.Holiday
import com.day.palette.presentation.utils.LivelyAdapter
import com.day.palette.presentation.utils.OnItemClickListener
import com.day.palette.presentation.utils.OnItemDoubleTapListener
import com.day.palette.presentation.utils.OnItemLongPressListener
import com.day.palette.presentation.utils.RECYCLER_ITEM_PARENT
import com.day.palette.presentation.utils.diffCallback
import com.day.palette.presentation.utils.getFormattedDate

class ExploreRecyclerAdapter(
    private val context: Context, private var countryHolidays: ArrayList<Holiday>
) : RecyclerView.Adapter<ExploreRecyclerAdapter.ExploreRecyclerViewHolder>(),
    LivelyAdapter<Holiday> {

    override var onItemClickListener: OnItemClickListener<Holiday>? = null
    override var onItemLongPressListener: OnItemLongPressListener<Holiday>? = null
    override var onItemDoubleTapListener: OnItemDoubleTapListener<Holiday>? = null

    class ExploreRecyclerViewHolder(b: CardHolidayBinding) : RecyclerView.ViewHolder(b.root) {
        val cardHoliday = b.cardHoliday
        val cardHolidayBgCL = b.cardHolidayBgCL
        val cardHolidayDayTV = b.cardHolidayDayTV
        val cardHolidayTypeTV = b.cardHolidayTypeTV
        val cardHolidayMonthTV = b.cardHolidayMonthTV
        val cardHolidayImportanceTV = b.cardHolidayImportanceTV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreRecyclerViewHolder {
        val b = CardHolidayBinding.inflate(
            LayoutInflater.from(context), parent, false
        )

        return ExploreRecyclerViewHolder(b)
    }

    override fun getItemCount(): Int {
        return countryHolidays.size
    }

    override fun onBindViewHolder(h: ExploreRecyclerViewHolder, position: Int) {
        val holiday = countryHolidays[position]

        h.cardHolidayBgCL.setBackgroundColor(holiday.bgColor)
        h.cardHolidayDayTV.text = holiday.date.getFormattedDate("dd")
        h.cardHolidayMonthTV.text = holiday.date.getFormattedDate("MMM, yyyy")
        h.cardHolidayTypeTV.text =
            if (holiday.global) context.getString(R.string.global) else context.getString(R.string.regional)
        h.cardHolidayImportanceTV.text = holiday.name

        h.cardHoliday.setOnClickListener {
            onItemClickListener?.invoke(position, holiday, RECYCLER_ITEM_PARENT)
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