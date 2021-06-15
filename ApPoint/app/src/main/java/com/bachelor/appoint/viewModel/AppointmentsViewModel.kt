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

    var liveAppointments: MutableLiveData<List<Appointment>> = MutableLiveData()

    fun getAppointments() : LiveData<List<Appointment>> {
        if (liveAppointments.value == null) {
            FirestoreClass().retrieveUserAppointments(this, activity = null)
        }

        return liveAppointments
    }

    fun successRetrieveAppointments(list: ArrayList<Appointment>) {
        liveAppointments.value = list
        liveAppointments.postValue(list)
    }

    fun addMockAppointment() {
//        FirestoreClass().addAppointment("11:30","10/10/2020", "unSS8p2PAUff9zuuJQIF", "Textila")
    }
}