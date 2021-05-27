package com.bachelor.appoint.model

import java.io.Serializable

class Appointment(
    val id: String = "",
    val u_id: String = "",
    val e_id: String = "",
    val event: String = "",
    var startTime: String = "",
    var date: String = "",
    var completed: Boolean = false,
    var status: String = "pending",
    var userName: String = "",
    var risk: Int = 0,
)