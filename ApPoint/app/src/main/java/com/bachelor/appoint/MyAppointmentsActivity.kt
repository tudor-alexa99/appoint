package com.bachelor.appoint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.appoint.ui.MyAppointmensAdapter
import com.bachelor.appoint.viewModel.AppointmentsViewModel

class MyAppointmentsActivity : AppCompatActivity() {
    private lateinit var appointmentsAdapter: MyAppointmensAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appointmentsViewModel: AppointmentsViewModel by viewModels()
//        appointmentsViewModel = ViewModelProvider(this).get(AppointmentsViewModel::class.java)
//        appointmentsViewModel.initialiseDbRef()
//        appointmentsViewModel.saveAppointment()
        setContentView(R.layout.activity_my_appointments)

        appointmentsAdapter = MyAppointmensAdapter(this, mutableListOf())

        appointmentsViewModel.liveAppointments.observe(this, Observer { appointments ->
            appointmentsAdapter.setAppointments(appointments)
        })

        appointmentsViewModel.getAppointments()

        var recyclerView = findViewById<RecyclerView>(R.id.my_appointments_recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = appointmentsAdapter

//        appointmentsViewModel = ViewModelProvider(this).get(AppointmentsViewModel::class.java)
//        appointmentsViewModel.getAppointmentsList().observe(this, Observer<List<Appointment>>() {
//        })
    }

    override fun onResume() {
        super.onResume()

        val appointmentsViewModel: AppointmentsViewModel by viewModels()

        appointmentsViewModel.getAppointments().observe(this, Observer { appointments ->
            appointmentsAdapter.setAppointments(appointments)
        })

    }
}