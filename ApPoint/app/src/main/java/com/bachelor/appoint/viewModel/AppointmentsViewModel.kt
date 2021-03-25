package com.bachelor.appoint.viewModel

import androidx.lifecycle.ViewModel
import com.bachelor.appoint.model.Appointment
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalTime

class AppointmentsViewModel() : ViewModel() {
    //    lateinit var appointments: MutableLiveData<List<Appointment>>
//
//    public fun getAppointmentsList(): LiveData<List<Appointment>> {
//        if (appointments.equals(null)) {
//            appointments = MutableLiveData()
//            initAppoointmentsList()
//        }
//
//        return appointments
//
//    }
//
//    private fun initAppoointmentsList() {
//        appointments.value = appointmentsList
//
//    }
//
    public fun saveAppointment() {
//        val rootNode = FirebaseDatabase.getInstance()
        val reference = Firebase.database.getReference("appointments")

        val appointmentId = reference.push().key

        // Get all the values
//        var place: String = "Test"
//        var startTime: LocalTime = LocalTime.of(10, 30, 0)
//        var completed: Boolean = false

        val testAppointment: Appointment = Appointment(
            appointmentId,
            "MetroSystems",
            LocalTime.of(20, 0, 0),
            false
        )

        if (appointmentId != null) {
            reference.child(appointmentId).setValue(testAppointment)
        }
    }
//
//    private fun loadAppointments() {
//
//    }
}