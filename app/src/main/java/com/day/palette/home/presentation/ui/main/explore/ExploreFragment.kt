package com.day.palette.home.presentation.ui.main.explore

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.day.palette.R
import com.day.palette.databinding.FragmentExploreBinding
import com.day.palette.home.domain.model.Holiday
import com.day.palette.common.presentation.utils.DP
import com.day.palette.common.presentation.utils.INSTANCE_POPULATED
import com.day.palette.common.presentation.utils.INSTANCE_RECYCLER_STATE
import com.day.palette.common.presentation.utils.currentState
import com.day.palette.common.presentation.utils.getThemeColor
import com.day.palette.common.presentation.utils.itemDecoration
import com.day.palette.common.presentation.utils.parcelable
import com.day.palette.common.presentation.utils.setOnItemClickListener
import com.day.palette.common.presentation.utils.showSnack
import com.day.palette.common.presentation.utils.showToast
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.viewmodel.observe


@AndroidEntryPoint
class ExploreFragment : Fragment() {

    private lateinit var b: FragmentExploreBinding
    private val vm: ExploreViewModel by viewModels()

    private lateinit var recyclerAdapter: ExploreRecyclerAdapter
    private lateinit var skeleton: Skeleton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        //Initialize View binding & setup Viewmodel observers
        b = FragmentExploreBinding.inflate(inflater, container, false)
        vm.observe(this, state = ::observeState, sideEffect = ::observeSideEffect)

        //Perform all the UI setup here
        setUpRecyclerView()
        setUpSkeleton()

        //Check if UI component is recreating itself
        if (savedInstanceState != null) {
            val recyclerState = savedInstanceState.parcelable<Parcelable>(INSTANCE_RECYCLER_STATE)
            b.exploreFragmentRV.layoutManager?.onRestoreInstanceState(recyclerState)
        } else {
            //Fetch all country holidays only if the data is not in the ViewModel
            if (vm.currentState.holidays.isEmpty()) {
                vm.invoke(ExploreIntent.GetWorldWideHolidays)
                skeleton.showSkeleton()
            }
        }

        return b.root
    }

    /**Observe changes in the State using Orbit StateFlow*/
    private fun observeState(state: ExploreState) {
        modifyRecyclerView(state.holidays)
    }

    /**Observe side effects using Orbit StateFlow*/
    private fun observeSideEffect(action: ExploreSideEffect) {
        when (action) {
            is ExploreSideEffect.ShowToast -> context?.showToast(action.message)
            is ExploreSideEffect.ShowSnack -> context?.showSnack(b.root, action.message)
        }

    }

    private fun setUpRecyclerView() {
        recyclerAdapter = ExploreRecyclerAdapter(requireContext(), ArrayList()).apply {
            setOnItemClickListener { position, holiday, type, _ ->
                //
            }
        }

        b.exploreFragmentRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
            isNestedScrollingEnabled = false
            itemDecoration(margin = 16.DP) { outRect, position, margin ->
                if (position == 0) outRect.top = margin
                outRect.bottom = margin
            }
        }
    }

    private fun setUpSkeleton() {
        val colorMask = requireContext().getThemeColor(id = R.attr.colorDivider)
        val colorShimmer = requireContext().getThemeColor(id = R.attr.colorShimmer)

        skeleton = b.exploreFragmentRV.applySkeleton(R.layout.card_holiday, 5).apply {
            maskCornerRadius = 16.DP.toFloat()
            shimmerDurationInMillis = 750
            maskColor = colorMask
            shimmerColor = colorShimmer
        }
    }

    private fun modifyRecyclerView(holidays: ArrayList<Holiday>) {
        if (holidays.isNotEmpty()) {
            recyclerAdapter.updateItems(holidays)
            if (skeleton.isSkeleton()) skeleton.showOriginal()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(INSTANCE_POPULATED, true)
        outState.putParcelable(
            INSTANCE_RECYCLER_STATE, b.exploreFragmentRV.layoutManager?.onSaveInstanceState()
        )
        super.onSaveInstanceState(outState)
    }
}