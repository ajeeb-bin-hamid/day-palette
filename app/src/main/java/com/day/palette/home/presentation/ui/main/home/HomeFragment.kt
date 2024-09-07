package com.day.palette.home.presentation.ui.main.home

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.text.Spannable
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.day.palette.R
import com.day.palette.databinding.FragmentHomeBinding
import com.day.palette.home.domain.model.Holiday
import com.day.palette.home.presentation.ui.holiday.HolidayActivity
import com.day.palette.home.presentation.ui.holiday.HolidayState
import com.day.palette.home.presentation.ui.main.home.sheets.ChangeCountrySheet
import com.day.palette.common.presentation.utils.DP
import com.day.palette.common.presentation.utils.INSTANCE_POPULATED
import com.day.palette.common.presentation.utils.INSTANCE_RECYCLER_STATE
import com.day.palette.common.presentation.utils.RECYCLER_ITEM_PARENT
import com.day.palette.common.presentation.utils.TypefaceSpan
import com.day.palette.common.presentation.utils.VIEW_MODEL_STATE
import com.day.palette.common.presentation.utils.currentState
import com.day.palette.common.presentation.utils.getThemeColor
import com.day.palette.common.presentation.utils.itemDecoration
import com.day.palette.common.presentation.utils.parcelable
import com.day.palette.common.presentation.utils.setOnItemClickListener
import com.day.palette.common.presentation.utils.showBottomSheet
import com.day.palette.common.presentation.utils.showSnack
import com.day.palette.common.presentation.utils.showToast
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
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
        vm.observe(this, state = ::observeState, sideEffect = ::observeSideEffect)

        //Perform all the UI setup here
        setUpRecyclerView()
        setUpSkeleton()
        setUpChangeCountryButton()

        //Check if UI component is recreating itself
        if (savedInstanceState != null) {
            val recyclerState = savedInstanceState.parcelable<Parcelable>(INSTANCE_RECYCLER_STATE)
            b.homeFragmentRV.layoutManager?.onRestoreInstanceState(recyclerState)
        } else {
            //Fetch all country holidays only if the data is not in the ViewModel
            if (vm.currentState.countryHolidays.isEmpty()) getCountryHolidays()
        }

        return b.root
    }


    /**Observe changes in the State using Orbit StateFlow*/
    private fun observeState(state: HomeState) {
        modifyTitle(state.selectedCountryName)
        modifyRecyclerView(state.countryHolidays)
    }

    /**Observe side effects using Orbit StateFlow*/
    private fun observeSideEffect(action: HomeSideEffect) {
        when (action) {
            is HomeSideEffect.ShowToast -> context?.showToast(action.message)
            is HomeSideEffect.ShowSnack -> context?.showSnack(b.root, action.message)
            is HomeSideEffect.GetCountryHolidays -> getCountryHolidays()
        }
    }

    private fun setUpRecyclerView() {
        recyclerAdapter = HomeRecyclerAdapter(requireContext(), ArrayList()).apply {
            setOnItemClickListener { _, holiday, type, sharedElements ->
                when (type) {
                    RECYCLER_ITEM_PARENT -> onClickHolidayItem(holiday, *sharedElements)
                }
            }
        }

        b.homeFragmentRV.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = recyclerAdapter
            isNestedScrollingEnabled = false
            itemDecoration(margin = 16.DP) { outRect, position, margin ->
                if (position == 0 || position == 1) outRect.top = margin
                if (position % 2 == 0) outRect.right = margin / 2
                if (position % 2 == 1) outRect.left = margin / 2
                outRect.bottom = margin
            }
        }
    }

    private fun setUpSkeleton() {
        val colorMask = requireContext().getThemeColor(id = R.attr.colorDivider)
        val colorShimmer = requireContext().getThemeColor(id = R.attr.colorShimmer)

        skeleton = b.homeFragmentRV.applySkeleton(R.layout.card_holiday_compact, 5).apply {
            maskCornerRadius = 16.DP.toFloat()
            shimmerDurationInMillis = 750
            maskColor = colorMask
            shimmerColor = colorShimmer
        }
    }

    private fun setUpChangeCountryButton() {
        b.homeFragmentChangeCountryButton.setOnClickListener {
            activity?.supportFragmentManager?.showBottomSheet(ChangeCountrySheet())
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

    private fun modifyRecyclerView(countryHolidays: ArrayList<Holiday>) {
        if (countryHolidays.isNotEmpty()) {
            recyclerAdapter.updateItems(countryHolidays)
            if (skeleton.isSkeleton()) skeleton.showOriginal()
        }
    }

    private fun getCountryHolidays() {
        vm.invoke(HomeIntent.GetCountryHolidays)
        skeleton.showSkeleton()
    }

    private fun onClickHolidayItem(holiday: Holiday, vararg sharedElements: Pair<View, String>) {
        activity?.let { activity ->
            if (sharedElements.isNotEmpty()) {
                val options: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, *sharedElements
                    )
                val i = Intent(activity, HolidayActivity::class.java).apply {
                    putExtra(VIEW_MODEL_STATE, HolidayState(holiday = holiday))
                    sharedElements.forEach { pair ->
                        putExtra(pair.second.replace(Regex("\\d+"), ""), pair.second)
                    }
                }

                startActivity(i, options.toBundle())
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(INSTANCE_POPULATED, true)
        outState.putParcelable(
            INSTANCE_RECYCLER_STATE, b.homeFragmentRV.layoutManager?.onSaveInstanceState()
        )
        super.onSaveInstanceState(outState)
    }


}