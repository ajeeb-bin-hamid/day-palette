package com.day.palette.presentation.ui.main.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.day.palette.databinding.FragmentExploreBinding


class ExploreFragment : Fragment() {

    private lateinit var b: FragmentExploreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentExploreBinding.inflate(inflater, container, false)

        return b.root
    }

}