package com.day.palette.presentation.ui.main.home.sheets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.day.palette.databinding.CardChangeCountryBinding
import com.day.palette.domain.model.Country
import com.day.palette.presentation.utils.AdaptiveRecyclerView
import com.day.palette.presentation.utils.diffCallback
import com.day.palette.presentation.utils.toPx

class ChangeCountryRecyclerAdapter(
    private val context: Context, private var allCountries: List<Country>
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
        h.cardChangeCountryRB.isChecked = country.isSelected


        h.cardChangeCountryRB.setOnClickListener { view ->
            /*        val currentSelectedCountryIndex = allCountries.indexOfFirst { it.isSelected }
                    if (currentSelectedCountryIndex != -1) allCountries.find { it.isSelected }?.isSelected =
                        false

                    allCountries.find { it.code == country.code }?.isSelected = true

                    if (currentSelectedCountryIndex != -1) notifyItemChanged(currentSelectedCountryIndex)*/

            onClickListener?.onClick(position, country, view)
        }

    }


    fun addItems(newItems: List<Country>, recyclerView: AdaptiveRecyclerView, screenHeight: Int) {
        val diffCallback = diffCallback(oldList = allCountries,
            newList = newItems,
            areItemsTheSame = { oldItem, newItem -> oldItem.code == newItem.code })
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        // Calculate the desired height based on the new data
        val newHeight = calculateDesiredHeight(newItems, screenHeight)
        recyclerView.animateHeight(newHeight)

        allCountries = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    private fun calculateDesiredHeight(newItems: List<Country>, screenHeight: Int): Int {
        val itemHeight = context.toPx(48)
        val contentHeight = (newItems.size * itemHeight) + context.toPx(16)

        // Use contentHeight if it's less than screenHeight, otherwise match_parent
        return if (contentHeight < screenHeight) contentHeight else screenHeight
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, country: Country, view: View)
    }
}