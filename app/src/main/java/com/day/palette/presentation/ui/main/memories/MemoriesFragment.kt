package com.day.palette.presentation.ui.main.memories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.day.palette.databinding.FragmentMemoriedBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MemoriesFragment : Fragment() {

    private lateinit var b: FragmentMemoriedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        b = FragmentMemoriedBinding.inflate(inflater, container, false)

        return b.root
    }

}