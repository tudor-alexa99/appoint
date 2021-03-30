package com.bachelor.appoint.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bachelor.appoint.model.Appointment
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalTime
import java.util.*

class AppointmentsViewModel() : ViewModel() {
    var appointmentList = mutableListOf<Appointment>()

    private lateinit var reference: DatabaseReference
    var liveAppointments: MutableLiveData<List<Appointment>> = MutableLiveData()

    fun getAppointments() : LiveData<List<Appointment>> {
        if(liveAppointments.value == null) {
            FirebaseDatabase.getInstance()
                .getReference("appointments")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            Log.d("onDataChange", snapshot.toString())
//                            liveAppointments.value = toAppointments(snapshot)
                            liveAppointments.postValue(toAppointments(snapshot))
                        }
                    }


                    override fun onCancelled(error: DatabaseError) {
                        Log.e("onCancelled", error.message)

                        TODO("Not yet implemented")
                    }

                })
        }
        Log.d("appointments", liveAppointments.value.toString())
        return liveAppointments
    }

    private fun toAppointments(snapshot: DataSnapshot): List<Appointment>? {
        var appointmentsList = mutableListOf<Appointment>()
        for (ds in snapshot.children) {
           appointmentsList.add(
               Appointment(
                   ds.child("id").toString(),
                   ds.child("place").toString(),
                   ds.child("startTime").toString(),
                   ds.child("completed").toString().toBoolean()
               )
           )
        }

        Log.d("toAppointments", appointmentsList.toString())
        return appointmentsList
    }

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