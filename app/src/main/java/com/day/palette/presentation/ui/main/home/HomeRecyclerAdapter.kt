package com.day.palette.presentation.ui.main.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.day.palette.databinding.CardHolidayCompactBinding
import com.day.palette.domain.model.Holiday
import com.day.palette.presentation.utils.diffCallback
import com.day.palette.presentation.utils.getFormattedDate

class HomeRecyclerAdapter(
    private val context: Context, private var countryHolidays: ArrayList<Holiday>
) : RecyclerView.Adapter<HomeRecyclerAdapter.HomeRecyclerViewHolder>() {

    private var onClickListener: OnClickListener? = null

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

        h.cardHolidayCompact.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, holiday, it)
            }
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

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, holiday: Holiday, view: View)
    }
}