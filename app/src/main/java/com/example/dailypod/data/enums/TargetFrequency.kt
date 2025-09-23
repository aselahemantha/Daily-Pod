package com.example.dailypod.data.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class TargetFrequency : Parcelable {
    DAILY,
    WEEKLY,
    MONTHLY,
    QUARTERLY,
    ANNUALLY,
}
