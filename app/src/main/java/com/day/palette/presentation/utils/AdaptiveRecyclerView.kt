package com.day.palette.presentation.utils

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class AdaptiveRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var noResultView: View? = null

    fun setNoResultView(view: View) {
        noResultView = view
        updateNoResultView()
    }

    fun animateHeight(newHeight: Int) {
        val currentHeight = layoutParams.height
        ValueAnimator.ofInt(currentHeight, newHeight).apply {
            duration = 500 // Duration of the animation
            addUpdateListener { animation ->
                val height = animation.animatedValue as Int
                layoutParams.height = height
                requestLayout()
            }
            start()
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                updateNoResultView()
            }
        })
        updateNoResultView()
    }

    private fun updateNoResultView() {
        noResultView?.let {
         //   it.visibility = if (layoutManager?.itemCount == 0) View.VISIBLE else View.GONE
        }
    }
}
