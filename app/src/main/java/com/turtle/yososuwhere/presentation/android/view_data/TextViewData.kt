package com.turtle.yososuwhere.presentation.android.view_data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TextViewData(
    val returnKey: String,
    val text: String
) : Parcelable