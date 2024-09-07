package com.day.palette.home.presentation.ui.holiday

import android.os.Parcelable
import com.day.palette.home.domain.model.Holiday
import kotlinx.parcelize.Parcelize

@Parcelize
data class HolidayState(
    val holiday: Holiday? = null
) : Parcelable