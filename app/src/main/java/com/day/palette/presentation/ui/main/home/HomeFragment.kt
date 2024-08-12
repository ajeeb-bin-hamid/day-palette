package com.day.palette.presentation.ui.main.home

import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.text.Spannable
import android.text.SpannableString
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.day.palette.R
import com.day.palette.databinding.FragmentHomeBinding
import com.day.palette.domain.model.Holiday
import com.day.palette.presentation.ui.main.home.sheets.ChangeCountrySheet
import com.day.palette.presentation.utils.TypefaceSpan
import com.day.palette.presentation.utils.itemDecoration
import com.day.palette.presentation.utils.parcelable
import com.day.palette.presentation.utils.toPx
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.viewmodel.observe


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var b: FragmentHomeBinding
    private val vm: HomeViewModel by activityViewModels()

    private lateinit var recyclerAdapter: HomeRecyclerAdapter
    private lateinit var skeleton: Skeleton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        //Initialize View binding & setup Viewmodel observers
        b = FragmentHomeBinding.inflate(inflater, container, false)
        vm.observe(this, state = ::observeState, sideEffect = ::observeIntent)

        //Perform all the UI setup here
        setUpRecyclerView()
        setUpSkeleton()
        setUpListeners()

        //Check if UI component is recreating itself
        if (savedInstanceState != null) {
            val recyclerState = savedInstanceState.parcelable<Parcelable>(INSTANCE_RECYCLER_STATE)
            b.homeFragmentRV.layoutManager?.onRestoreInstanceState(recyclerState)
        } else {
            //API call to fetch selected country holidays
            vm.invoke(HomeIntent.GetCountryHolidays)
            skeleton.showSkeleton()
        }

        return b.root
    }


    /**Observe changes in the State using Orbit StateFlow*/
    private fun observeState(state: HomeState) {
        modifyTitle(state.selectedCountryName)
        modifyRecyclerView(state.countryHolidays)
    }

    /**Observe side effects using Orbit StateFlow*/
    private fun observeIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ShowToast -> {
                context?.let {
                    Toast.makeText(it, intent.message.asString(it), Toast.LENGTH_SHORT).show()
                }

            }

            is HomeIntent.ShowSnack -> {
                context?.let {
                    Snackbar.make(b.root, intent.message.asString(it), Snackbar.LENGTH_SHORT).show()
                }
            }

            is HomeIntent.GetCountryHolidays -> {
                //API call to fetch selected country holidays
                vm.invoke(HomeIntent.GetCountryHolidays)
                skeleton.showSkeleton()
            }

            else -> {
                //
            }
        }
    }

    private fun setUpRecyclerView() {
        recyclerAdapter = HomeRecyclerAdapter(requireContext(), ArrayList()).apply {
            setOnClickListener(object : HomeRecyclerAdapter.OnClickListener {
                override fun onClick(position: Int, holiday: Holiday, view: View) {
                    //
                }

            })
        }

        b.homeFragmentRV.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = recyclerAdapter
            isNestedScrollingEnabled = false
            itemDecoration(margin = requireContext().toPx(16)) { outRect, position, margin ->
                if (position == 0 || position == 1) outRect.top = margin
                if (position % 2 == 0) outRect.right = margin / 2
                if (position % 2 == 1) outRect.left = margin / 2
                outRect.bottom = margin
            }
        }
    }

    private fun setUpSkeleton() {
        val maskTypedValue = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.colorDivider, maskTypedValue, true)
        val colorMask = ContextCompat.getColor(requireContext(), maskTypedValue.resourceId)

        val shimmerTypedValue = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.colorShimmer, shimmerTypedValue, true)
        val colorShimmer = ContextCompat.getColor(requireContext(), shimmerTypedValue.resourceId)

        skeleton = b.homeFragmentRV.applySkeleton(R.layout.card_holiday_compact, 5).apply {
            maskCornerRadius = requireContext().toPx(24).toFloat()
            shimmerDurationInMillis = 750
            maskColor = colorMask
            shimmerColor = colorShimmer
        }
    }

    private fun setUpListeners() {
        b.homeFragmentChangeCountryButton.setOnClickListener {
            activity?.let {
                ChangeCountrySheet().show(
                    it.supportFragmentManager, ChangeCountrySheet.TAG
                )
            }
        }
    }

    private fun modifyTitle(selectedCountryName: String) {
        context?.let { cxt ->
            val boldFont: Typeface? = ResourcesCompat.getFont(cxt, R.font.poppins_bold)

            // Create a SpannableString
            val prefixString = getString(R.string.future_holidays_in)
            val spannableString = SpannableString("$prefixString $selectedCountryName")

            // Apply bold font to a specific portion
            boldFont?.let {
                val customTypefaceSpan = TypefaceSpan("", it)
                spannableString.setSpan(
                    customTypefaceSpan,
                    prefixString.length,
                    prefixString.length + selectedCountryName.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            // Set the SpannableString to the TextView
            b.homeFragmentTitleTV.text = spannableString
        }

    }

    private fun modifyRecyclerView(countryHolidays: List<Holiday>) {
        if (countryHolidays.isNotEmpty()) {
            recyclerAdapter.appendItems(countryHolidays)
            skeleton.showOriginal()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(INSTANCE_POPULATED, true)
        outState.putParcelable(
            INSTANCE_RECYCLER_STATE, b.homeFragmentRV.layoutManager?.onSaveInstanceState()
        )
        super.onSaveInstanceState(outState)
    }

    companion object {
        //keys for handling savedInstanceState
        const val INSTANCE_POPULATED = "instance_populated"
        const val INSTANCE_RECYCLER_STATE = "instance_recycler_state"
    }

}