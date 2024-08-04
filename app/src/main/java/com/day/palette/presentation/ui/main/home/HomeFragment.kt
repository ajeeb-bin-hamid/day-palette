package com.day.palette.presentation.ui.main.home

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.day.palette.R
import com.day.palette.databinding.FragmentHomeBinding
import com.day.palette.presentation.utils.TypefaceSpan
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.observe


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var b: FragmentHomeBinding
    private val vm: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentHomeBinding.inflate(inflater, container, false)

        vm.observe(this, state = ::observeState)
        observeIntent()

        return b.root
    }

    /**Observe changes in the State using Orbit StateFlow*/
    private fun observeState(state: HomeState) {
        setUpTitle(state.selectedCountryName)
    }

    /**Observe for new intents using SharedFlow*/
    private fun observeIntent() {
        lifecycleScope.launch {
            vm.uiActions.collect { intent ->
                when (intent) {

                    else -> {
                        //
                    }
                }
            }
        }
    }

    private fun setUpTitle(selectedCountryName: String?) {
        val countryName = selectedCountryName ?: getString(R.string.default_country_name)

        val boldFont: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.poppins_bold)

        // Create a SpannableString
        val spannableString = SpannableString("Future holidays in United States")

        // Apply bold font to a specific portion
        boldFont?.let {
            val customTypefaceSpan = TypefaceSpan("", it)
            spannableString.setSpan(
                customTypefaceSpan, 19, 19 + countryName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        // Set the SpannableString to the TextView
        b.homeFragmentTitleTV.text = spannableString

    }

}