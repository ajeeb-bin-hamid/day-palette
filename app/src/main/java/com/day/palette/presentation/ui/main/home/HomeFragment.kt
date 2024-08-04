package com.day.palette.presentation.ui.main.home

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.day.palette.R
import com.day.palette.databinding.FragmentHomeBinding
import com.day.palette.presentation.utils.TypefaceSpan
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.viewmodel.observe


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var b: FragmentHomeBinding
    private val vm: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentHomeBinding.inflate(inflater, container, false)

        vm.observe(this, state = ::observeState, sideEffect = ::observeIntent)

        vm.invoke(HomeIntent.GetCountryHolidays)

        b.homeFragmentTitleTV.setOnClickListener {
            vm.invoke(HomeIntent.GetCountryHolidays)
        }

        return b.root
    }

    /**Observe changes in the State using Orbit StateFlow*/
    private fun observeState(state: HomeState) {
        setUpTitle(state.selectedCountryName)


    }

    /**Observe side effects using Orbit StateFlow*/
    private fun observeIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ShowToast -> {
                Toast.makeText(requireContext(), intent.message, Toast.LENGTH_SHORT).show()
            }

            is HomeIntent.ShowSnack -> {
                Snackbar.make(b.root, intent.message, Snackbar.LENGTH_SHORT).show()
            }

            else -> {
                //
            }
        }
    }

    private fun setUpTitle(selectedCountryName: String) {
        val boldFont: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.poppins_bold)

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