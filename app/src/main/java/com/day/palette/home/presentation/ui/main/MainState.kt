package com.day.palette.home.presentation.ui.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainState(
    val isLoading: Boolean = false
) : Parcelable
