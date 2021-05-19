package com.bachelor.appoint.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Event(
    val adminId: String = "",
    val name: String = "",
    val location: String = "",
    val phoneNumber: String = "",
    val type: String = "",
    val estimatedSurface: Int = 0,
    val seatsNumber: Int = 0,
    val openSpace: Boolean = false,
    val estimatedRisk: Int = 0,
    val duration: String = "",
    var id: String = "",
) : Parcelable