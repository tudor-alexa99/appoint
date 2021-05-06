package com.bachelor.appoint.model

import java.io.Serializable

class Appointment(
    val id: String = "",
    val u_id: String = "",
    val b_id: String = "",
    val business: String = "",
    var startTime: String = "",
    var date: String = "",
    var completed: Boolean = false,
    var status: String = "pending",
)