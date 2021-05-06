package com.bachelor.appoint.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.model.Appointment
import com.google.firebase.database.*
import java.util.*

class AppointmentsViewModel() : ViewModel() {
    var appointmentList = mutableListOf<Appointment>()

    private lateinit var reference: DatabaseReference
    var liveAppointments: MutableLiveData<List<Appointment>> = MutableLiveData()

    fun getAppointments() : LiveData<List<Appointment>> {
//        if(liveAppointments.value == null) {
//            FirebaseDatabase.getInstance()
//                .getReference("appointments")
//                .addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        if (snapshot.exists()) {
//                            Log.d("onDataChange", snapshot.toString())
//                            liveAppointments.value = toAppointments(snapshot)
//                            liveAppointments.postValue(toAppointments(snapshot))
//                        }
//                    }
//
//
//                    override fun onCancelled(error: DatabaseError) {
//                        Log.e("onCancelled", error.message)
//
//                        TODO("Not yet implemented")
//                    }
//
//                })
//        }
        if (liveAppointments.value == null) {
            FirestoreClass().retrieveUserAppointments(this)
        }

        Log.d("liveAppointments", liveAppointments.toString())
        return liveAppointments
    }

    fun successRetrieveAppointments(list: ArrayList<Appointment>) {
        liveAppointments.value = list
        liveAppointments.postValue(list)
    }

    fun addMockAppointment() {
        FirestoreClass().addAppointment("11:30","10/10/2020", "unSS8p2PAUff9zuuJQIF", "Textila")
    }

//    private fun toAppointments(snapshot: DataSnapshot): List<Appointment>? {
//        val appointmentsList = mutableListOf<Appointment>()
//        for (ds in snapshot.children) {
//            Log.d("Current DS", ds.child("place").toString())
//           appointmentsList.add(
//               Appointment(
//                   ds.child("id").value.toString(),
//                   ds.child("place").value.toString(),
//                   ds.child("startTime").value.toString(),
//                   ds.child("completed").value.toString().toBoolean()
//               )
//           )
//        }
//
//        Log.d("toAppointments", appointmentsList.toString())
//        return appointmentsList
//    }

//    fun initialiseDbRef() {
//        reference = Firebase.database.getReference("appointments")
//        reference.addValueEventListener(appointmentListener)
//        appointmentList = mutableListOf()
//    }

//    val appointmentListener = object : ValueEventListener {
//        override fun onDataChange(snapshot: DataSnapshot) {
//            Log.d("Snapshot", snapshot.toString())
//            appointmentList.add(
//                Appointment(
//                    snapshot.child("id").toString(),
//                    snapshot.child("place").toString(),
//                    snapshot.child("startTime").toString(),
//                    snapshot.child("completed").toString().toBoolean()
//                )
//            )
//        }
//
//        override fun onCancelled(error: DatabaseError) {
//            Log.w(TAG, "loadPost:onCancelled", error.toException())
//        }
//    }
//

//    fun getData(): MutableList<Appointment> {
//        Log.d("getData", appointmentList.toString())
//        return appointmentList
//    }

//    fun saveAppointment() {
//        val appointmentId = reference.push().key
//
//        val testAppointment = Appointment(
//            appointmentId,
//            "MetroSystems",
//            "12:00:00",
//            false
//        )
//
//        if (appointmentId != null) {
//            reference.child(appointmentId).setValue(testAppointment)
//        }
//    }

//
}