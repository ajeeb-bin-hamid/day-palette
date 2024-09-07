package com.day.palette.home.presentation.ui.main.explore

import android.os.Parcelable
import com.day.palette.home.domain.model.Holiday
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExploreState(
    val holidays: ArrayList<Holiday>
) : Parcelable
