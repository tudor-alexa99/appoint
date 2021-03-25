package com.bachelor.appoint.model

import java.io.Serializable
import java.time.LocalTime

class Appointment(val id: String?, var place: String, var startTime: LocalTime, var completed: Boolean): Serializable {
}