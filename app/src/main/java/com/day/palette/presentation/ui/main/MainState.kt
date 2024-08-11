package com.day.palette.presentation.ui.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainState(
    val isLoading: Boolean = false
) : Parcelable
