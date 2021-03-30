package com.bachelor.appoint.model

import java.io.Serializable

class Appointment(val id: String?, var place: String, var startTime: String, var completed: Boolean): Serializable {
}