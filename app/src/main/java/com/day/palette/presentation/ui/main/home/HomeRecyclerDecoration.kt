package com.day.palette.presentation.ui.main.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HomeRecyclerDecoration(private val margin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == 0 || position == 1) outRect.top = margin
        if (position % 2 == 0) outRect.right = margin / 2
        if (position % 2 == 1) outRect.left = margin / 2
        outRect.bottom = margin
    }
}