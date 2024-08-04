package com.day.palette.presentation.ui.main.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.day.palette.databinding.CardHolidayMiniBinding
import com.day.palette.domain.model.Holiday

class HomeRecyclerAdapter(
    private val context: Context, private val countryHolidays: ArrayList<Holiday>
) : RecyclerView.Adapter<HomeRecyclerAdapter.HomeRecyclerViewHolder>() {

    private var onClickListener: OnClickListener? = null

    class HomeRecyclerViewHolder(b: CardHolidayMiniBinding) : RecyclerView.ViewHolder(b.root) {
        val cardHolidayMini = b.cardHolidayMini
        val cardHolidayMiniDayTV = b.cardHolidayMiniDayTV
        val cardHolidayMiniMonthTV = b.cardHolidayMiniMonthTV
        val cardHolidayMiniImportanceTV = b.cardHolidayMiniImportanceTV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerViewHolder {
        val b = CardHolidayMiniBinding.inflate(
            LayoutInflater.from(context), parent, false
        )

        return HomeRecyclerViewHolder(b)
    }

    override fun getItemCount(): Int {
        return countryHolidays.size
    }

    override fun onBindViewHolder(h: HomeRecyclerViewHolder, position: Int) {
        val holiday = countryHolidays[position]

        h.cardHolidayMiniImportanceTV.text = holiday.name

        h.cardHolidayMini.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, holiday, it)
            }
        }

    }

    fun appendItems(newItems: List<Holiday>) {
        val startIndex = countryHolidays.size
        countryHolidays.addAll(newItems)
        notifyItemRangeInserted(startIndex, newItems.size)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, holiday: Holiday, view: View)
    }
}