package com.bachelor.appoint.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Business(
    val adminId: String = "",
    val name: String = "",
    val location: String = "",
    val phoneNumber: String = "",
    val type: String = "",
    var id: String = "",
) : Parcelable