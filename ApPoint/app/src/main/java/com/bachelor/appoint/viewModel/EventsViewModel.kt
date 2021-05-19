package com.bachelor.appoint.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.model.Event

class EventsViewModel(): ViewModel() {
    var businessesList = mutableListOf<Event>()
    var liveBusinesses: MutableLiveData<List<Event>> = MutableLiveData()


//    fun getBusinesses(): LiveData<List<Business>> {
//        if(liveBusinesses.value == null) {
//            FirestoreClass().retrieveBusinesses()
//        }
//    }
}