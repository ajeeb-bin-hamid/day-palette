package com.day.palette.presentation.ui.main.home.sheets

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.day.palette.R
import com.day.palette.databinding.SheetChangeCountryBinding
import com.day.palette.domain.model.Country
import com.day.palette.presentation.ui.main.home.HomeIntent
import com.day.palette.presentation.ui.main.home.HomeRecyclerDecoration
import com.day.palette.presentation.ui.main.home.HomeState
import com.day.palette.presentation.ui.main.home.HomeViewModel
import com.day.palette.presentation.utils.parcelable
import com.day.palette.presentation.utils.toPx
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.orbitmvi.orbit.viewmodel.observe

class ChangeCountrySheet : BottomSheetDialogFragment() {

    private lateinit var b: SheetChangeCountryBinding
    private val vm: HomeViewModel by activityViewModels()

    private lateinit var recyclerAdapter: ChangeCountryRecyclerAdapter
    private lateinit var skeleton: Skeleton
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.sheet_change_country, null)
        bottomSheet.setContentView(view)

        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        return bottomSheet
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        //Initialize View binding & setup Viewmodel observers
        b = SheetChangeCountryBinding.inflate(inflater, container, false)
        vm.observe(this, state = ::observeState)

        //Perform all the UI setup here
        setUpRecyclerView()
        setUpSkeleton()


        b.changeCountrySheetET.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        //Check if UI component is recreating itself
        if (savedInstanceState != null) {
            val recyclerState = savedInstanceState.parcelable<Parcelable>(INSTANCE_RECYCLER_STATE)
            b.changeCountrySheetRV.layoutManager?.onRestoreInstanceState(recyclerState)
        } else {
            //Fetch all countries only if the data is not in the ViewModel
            if (vm.container.stateFlow.value.allCountries.isEmpty()) {
                vm.invoke(HomeIntent.GetAllCountries)
                skeleton.showSkeleton()
            }
        }

        return b.root
    }

    /**Observe changes in the State using Orbit StateFlow*/
    private fun observeState(state: HomeState) {
        modifyRecyclerView(state.allCountries)
    }

    private fun setUpRecyclerView() {
        recyclerAdapter = ChangeCountryRecyclerAdapter(requireContext(), ArrayList()).apply {
            setOnClickListener(object : ChangeCountryRecyclerAdapter.OnClickListener {
                override fun onClick(position: Int, holiday: Country, view: View) {
                    //
                }
            })
        }

        b.changeCountrySheetRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
            isNestedScrollingEnabled = false
            addItemDecoration(HomeRecyclerDecoration(requireContext().toPx(16)))
        }
    }

    private fun setUpSkeleton() {
        val maskTypedValue = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.colorDivider, maskTypedValue, true)
        val colorMask = ContextCompat.getColor(requireContext(), maskTypedValue.resourceId)

        val shimmerTypedValue = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.colorShimmer, shimmerTypedValue, true)
        val colorShimmer = ContextCompat.getColor(requireContext(), shimmerTypedValue.resourceId)

        skeleton = b.changeCountrySheetRV.applySkeleton(R.layout.card_change_country, 5).apply {
            maskCornerRadius = requireContext().toPx(24).toFloat()
            shimmerDurationInMillis = 750
            maskColor = colorMask
            shimmerColor = colorShimmer
        }
    }

    private fun modifyRecyclerView(allCountries: List<Country>) {
        if (allCountries.size > recyclerAdapter.itemCount) {
            recyclerAdapter.appendItems(allCountries)
            skeleton.showOriginal()
        }
    }

    override fun getTheme(): Int = R.style.Sheet_ColorSystem

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(INSTANCE_POPULATED, true)
        outState.putParcelable(
            INSTANCE_RECYCLER_STATE, b.changeCountrySheetRV.layoutManager?.onSaveInstanceState()
        )
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val TAG = "ChangeCountrySheet"

        //keys for handling savedInstanceState
        const val INSTANCE_POPULATED = "instance_populated"
        const val INSTANCE_RECYCLER_STATE = "instance_recycler_state"
    }
}