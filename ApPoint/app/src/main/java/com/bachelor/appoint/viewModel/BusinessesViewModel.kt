package com.bachelor.appoint.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.model.Business

class BusinessesViewModel(): ViewModel() {
    var businessesList = mutableListOf<Business>()
    var liveBusinesses: MutableLiveData<List<Business>> = MutableLiveData()


//    fun getBusinesses(): LiveData<List<Business>> {
//        if(liveBusinesses.value == null) {
//            FirestoreClass().retrieveBusinesses()
//        }
//    }
}