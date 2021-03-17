package com.bachelor.appoint.model

import java.io.Serializable
import java.time.LocalTime

class Appointment(var place: String, var startTime: LocalTime, var completed: Boolean): Serializable {
}