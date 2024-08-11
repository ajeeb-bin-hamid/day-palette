package com.day.palette.presentation.ui.main.home.sheets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.day.palette.databinding.CardChangeCountryBinding
import com.day.palette.domain.model.Country
import com.day.palette.domain.model.Holiday

class ChangeCountryRecyclerAdapter(
    private val context: Context, private val allCountries: ArrayList<Country>
) : RecyclerView.Adapter<ChangeCountryRecyclerAdapter.ChangeCountryRecyclerViewHolder>() {

    private var onClickListener: OnClickListener? = null

    class ChangeCountryRecyclerViewHolder(b: CardChangeCountryBinding) :
        RecyclerView.ViewHolder(b.root) {
        val cardChangeCountryRB = b.cardChangeCountryRB
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ChangeCountryRecyclerViewHolder {
        val b = CardChangeCountryBinding.inflate(
            LayoutInflater.from(context), parent, false
        )

        return ChangeCountryRecyclerViewHolder(b)
    }

    override fun getItemCount(): Int {
        return allCountries.size
    }

    override fun onBindViewHolder(h: ChangeCountryRecyclerViewHolder, position: Int) {
        val country = allCountries[position]

        h.cardChangeCountryRB.text = country.name

        /* h.cardChangeCountryRB.setOnClickListener {
             if (onClickListener != null) {
                 onClickListener!!.onClick(position, holiday, it)
             }
         }*/

    }


    fun appendItems(newItems: List<Country>) {
        val startIndex = allCountries.size
        allCountries.addAll(newItems)
        notifyItemRangeInserted(startIndex, newItems.size)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, holiday: Country, view: View)
    }
}