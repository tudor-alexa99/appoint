package com.bachelor.appoint

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.appoint.data.appointmentsList
import com.bachelor.appoint.model.Appointment
import com.bachelor.appoint.ui.MyAppointmensAdapter
import com.bachelor.appoint.viewModel.AppointmentsViewModel
import com.google.firebase.database.DataSnapshot

class MyAppointmentsActivity : AppCompatActivity() {
    private lateinit var appointmensAdapter: MyAppointmensAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appointmentsViewModel: AppointmentsViewModel by viewModels()
//        appointmentsViewModel = ViewModelProvider(this).get(AppointmentsViewModel::class.java)
        appointmentsViewModel.initialiseDbRef()
//        appointmentsViewModel.saveAppointment()
        setContentView(R.layout.activity_my_appointments)

        appointmensAdapter = MyAppointmensAdapter(this, appointmentsViewModel.getData())

        var recyclerView = findViewById<RecyclerView>(R.id.my_appointments_recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = appointmensAdapter

//        appointmentsViewModel = ViewModelProvider(this).get(AppointmentsViewModel::class.java)
//        appointmentsViewModel.getAppointmentsList().observe(this, Observer<List<Appointment>>() {
//        })
    }
}