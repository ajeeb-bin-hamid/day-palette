package com.day.palette.presentation.ui.main.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.day.palette.databinding.CardHolidayCompactBinding
import com.day.palette.domain.model.Holiday
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeRecyclerAdapter(
    private val context: Context, private val countryHolidays: ArrayList<Holiday>
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
        h.cardHolidayCompactDayTV.text = getFormattedDate(holiday.date, "dd")
        h.cardHolidayCompactMonthTV.text = getFormattedDate(holiday.date, "MMM, yyyy")
        h.cardHolidayCompactImportanceTV.text = holiday.name

        h.cardHolidayCompact.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, holiday, it)
            }
        }

    }

    private fun getFormattedDate(dateString: String, exp: String): String {
        // Define the date format
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        // Parse the date string into a Date object
        val date: Date = dateFormat.parse(dateString) ?: return ""
        // Define a format to extract only the day part
        val dayFormat = SimpleDateFormat(exp, Locale.US)
        // Format the Date object to extract the day
        return dayFormat.format(date)
    }

    fun appendItems(newItems: List<Holiday>) {
        val startIndex = countryHolidays.size
        countryHolidays.addAll(newItems)
        notifyItemRangeInserted(startIndex, newItems.size)
        println("infox, yoyo")
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, holiday: Holiday, view: View)
    }
}