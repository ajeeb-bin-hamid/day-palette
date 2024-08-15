package com.day.palette.presentation.ui.main.home.sheets

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.os.Parcelable
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.day.palette.R
import com.day.palette.databinding.SheetChangeCountryBinding
import com.day.palette.domain.model.Country
import com.day.palette.domain.model.SelectedCountryDetails
import com.day.palette.presentation.ui.main.home.HomeIntent
import com.day.palette.presentation.ui.main.home.HomeState
import com.day.palette.presentation.ui.main.home.HomeViewModel
import com.day.palette.presentation.utils.itemDecoration
import com.day.palette.presentation.utils.onTextChanged
import com.day.palette.presentation.utils.parcelable
import com.day.palette.presentation.utils.toPx
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.observe
import javax.inject.Inject


@AndroidEntryPoint
class ChangeCountrySheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var displayMetrics: DisplayMetrics

    private lateinit var b: SheetChangeCountryBinding
    private val vm: HomeViewModel by activityViewModels()

    private lateinit var recyclerAdapter: ChangeCountryRecyclerAdapter
    private lateinit var skeleton: Skeleton
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private var debounceJob: Job? = null

    private var availableScreenSpace: Int = 0

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
        setUpInsets()
        setUpRecyclerView()
        setUpSkeleton()
        setUpSearchEditText()

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

    private fun setUpInsets() {
        availableScreenSpace = getScreenHeight(requireContext())
        ViewCompat.setOnApplyWindowInsetsListener(
            requireActivity().window.decorView
        ) { _: View?, insets: WindowInsetsCompat ->

            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val keyboardHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom

            context?.let { ctx ->
                if (isKeyboardVisible) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    availableScreenSpace = availableScreenSpace - keyboardHeight + systemBars.bottom
                    bottomSheetBehavior.peekHeight = displayMetrics.heightPixels - keyboardHeight
                } else {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    availableScreenSpace = getScreenHeight(ctx)
                }

                b.changeCountrySheetRV.apply {
                    animateHeight(calculateDesiredHeight(adapter?.itemCount ?: 1))
                }
            }

            insets
        }
    }

    private fun setUpRecyclerView() {
        recyclerAdapter = ChangeCountryRecyclerAdapter(requireContext(), ArrayList()).apply {
            setOnClickListener(object : ChangeCountryRecyclerAdapter.OnClickListener {
                override fun onClick(position: Int, country: Country, view: View) {
                    onClickCountry(country)
                }
            })
        }

        b.changeCountrySheetRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
            isNestedScrollingEnabled = true
            setNoResultView(b.changeCountrySheetNoResultView.root)
            layoutParams.height = getScreenHeight(requireContext())
            itemDecoration(margin = requireContext().toPx(16)) { outRect, position, margin ->
                if (position == 0) outRect.top = margin
                outRect.left = margin
                outRect.right = margin
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

        skeleton = b.changeCountrySheetRV.applySkeleton(R.layout.card_change_country, 10).apply {
            maskCornerRadius = requireContext().toPx(24).toFloat()
            shimmerDurationInMillis = 750
            maskColor = colorMask
            shimmerColor = colorShimmer
        }
    }

    private fun setUpSearchEditText() {/*   b.changeCountrySheetET.setOnFocusChangeListener { _, hasFocus ->
               if (hasFocus) bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
           }*/

        b.changeCountrySheetET.onTextChanged(afterTextChanged = { searchString ->
            debounceJob?.cancel()
            debounceJob = lifecycleScope.launch {
                delay(200)
                val searchResults = vm.container.stateFlow.value.allCountries.filter {
                    it.name.contains(
                        searchString, ignoreCase = true
                    ) || it.code.contains(searchString, ignoreCase = true)
                }
                recyclerAdapter.addItems(
                    searchResults, b.changeCountrySheetRV, availableScreenSpace
                )
            }
        })
    }


    private fun modifyRecyclerView(allCountries: List<Country>) {
        if (allCountries.isNotEmpty()) {
            recyclerAdapter.addItems(allCountries, b.changeCountrySheetRV, availableScreenSpace)
            if (skeleton.isSkeleton()) skeleton.showOriginal()
        }
    }

    private fun onClickCountry(country: Country) {
        lifecycleScope.launch {
            delay(200)
            dismiss()
            vm.invoke(
                HomeIntent.SetSelectedCountry(SelectedCountryDetails(country.name, country.code))
            )
        }
    }

    private fun getScreenHeight(context: Context): Int {
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        val margin = 16 + 40 + 32 + 4 + 16
        return screenHeight - context.toPx(margin)
    }

    private fun calculateDesiredHeight(itemCount: Int): Int {
        context?.let { ctx ->
            val itemHeight = ctx.toPx(48)
            val contentHeight = (itemCount * itemHeight) + ctx.toPx(16)

            // Use contentHeight if it's less than screenHeight, otherwise match_parent
            return if (contentHeight < availableScreenSpace) contentHeight else availableScreenSpace
        }
        return availableScreenSpace
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