package com.day.palette.presentation.ui.main.home.sheets

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.os.Parcelable
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.day.palette.presentation.utils.DP
import com.day.palette.presentation.utils.INSTANCE_POPULATED
import com.day.palette.presentation.utils.INSTANCE_RECYCLER_STATE
import com.day.palette.presentation.utils.RECYCLER_ITEM_PARENT
import com.day.palette.presentation.utils.currentState
import com.day.palette.presentation.utils.getThemeColor
import com.day.palette.presentation.utils.itemDecoration
import com.day.palette.presentation.utils.onTextChanged
import com.day.palette.presentation.utils.parcelable
import com.day.palette.presentation.utils.setOnItemClickListener
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
            if (vm.currentState.allCountries.isEmpty()) {
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
        availableScreenSpace = getScreenHeight()
        ViewCompat.setOnApplyWindowInsetsListener(
            requireActivity().window.decorView
        ) { _: View?, insets: WindowInsetsCompat ->

            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val keyboardHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom

            if (isKeyboardVisible) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                availableScreenSpace = availableScreenSpace - keyboardHeight + systemBars.bottom
                bottomSheetBehavior.peekHeight = displayMetrics.heightPixels - keyboardHeight
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                availableScreenSpace = getScreenHeight()
            }

            b.changeCountrySheetRV.apply {
                animateHeight(calculateDesiredHeight(adapter?.itemCount ?: 1))
            }
            insets
        }
    }

    private fun setUpRecyclerView() {
        recyclerAdapter = ChangeCountryRecyclerAdapter(requireContext(), ArrayList()).apply {
            setOnItemClickListener { _, country, type ->
                when (type) {
                    RECYCLER_ITEM_PARENT -> onClickCountry(country)
                }
            }
        }

        b.changeCountrySheetRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
            isNestedScrollingEnabled = true
            setNoResultView(b.changeCountrySheetNoResultView.root)
            layoutParams.height = getScreenHeight()
            itemDecoration(margin = 16.DP) { outRect, position, margin ->
                if (position == 0) outRect.top = margin
                outRect.left = margin
                outRect.right = margin
                outRect.bottom = margin
            }
        }
    }

    private fun setUpSkeleton() {
        val colorMask = requireContext().getThemeColor(id = R.attr.colorDivider)
        val colorShimmer = requireContext().getThemeColor(id = R.attr.colorShimmer)

        skeleton = b.changeCountrySheetRV.applySkeleton(R.layout.card_change_country, 10).apply {
            maskCornerRadius = 16.DP.toFloat()
            shimmerDurationInMillis = 750
            maskColor = colorMask
            shimmerColor = colorShimmer
        }
    }

    private fun setUpSearchEditText() {
        b.changeCountrySheetET.onTextChanged(afterTextChanged = { searchString ->
            debounceJob?.cancel()
            debounceJob = lifecycleScope.launch {
                delay(200)
                val searchResults = vm.currentState.allCountries.filter {
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

            val selectedCountry = SelectedCountryDetails(
                selectedCountryName = country.name, selectedCountryCode = country.code
            )
            vm.invoke(HomeIntent.SetSelectedCountry(selectedCountry))
        }
    }

    private fun getScreenHeight(): Int {
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        val margin = 16 + 40 + 32 + 4 + 16
        return screenHeight - margin.DP
    }

    private fun calculateDesiredHeight(itemCount: Int): Int {
        val itemHeight = 48.DP
        val contentHeight = (itemCount * itemHeight) + 16.DP

        // Use contentHeight if it's less than screenHeight, otherwise match_parent
        return if (contentHeight < availableScreenSpace) contentHeight else availableScreenSpace
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
    }
}