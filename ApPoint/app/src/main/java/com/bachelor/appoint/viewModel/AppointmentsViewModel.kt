package com.bachelor.appoint.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.bachelor.appoint.model.Appointment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalTime

class AppointmentsViewModel() : ViewModel() {
    lateinit var appointmentList: MutableList<Appointment>
    private lateinit var reference: DatabaseReference

    fun initialiseDbRef() {
        reference = Firebase.database.getReference("appointments")
        reference.addValueEventListener(appointmentListener)
        appointmentList = mutableListOf()
    }

    val appointmentListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            Log.d("Snapshot", snapshot.toString())
            appointmentList.add(
                Appointment(
                    snapshot.child("id").toString(),
                    snapshot.child("place").toString(),
                    snapshot.child("startTime").toString(),
                    snapshot.child("completed").toString().toBoolean()
                )
            )
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w(TAG, "loadPost:onCancelled", error.toException())
        }
    }


    fun getData(): MutableList<Appointment> {
        Log.d("getData", appointmentList.toString())
        return appointmentList
    }

    fun saveAppointment() {
        val appointmentId = reference.push().key

        val testAppointment = Appointment(
            appointmentId,
            "MetroSystems",
            "12:00:00",
            false
        )

        if (appointmentId != null) {
            reference.child(appointmentId).setValue(testAppointment)
        }
    }

//
}