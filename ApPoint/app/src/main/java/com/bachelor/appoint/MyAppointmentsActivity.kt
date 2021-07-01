package com.bachelor.appoint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.appoint.adapters.MyAppointmensAdapter
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.ActivityMyAppointmentsBinding
import com.bachelor.appoint.model.Appointment
import com.bachelor.appoint.viewModel.AppointmentsViewModel

class MyAppointmentsActivity : AppCompatActivity() {
    private lateinit var appointmentsAdapter: MyAppointmensAdapter
    private lateinit var binding: ActivityMyAppointmentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the binding
        binding = ActivityMyAppointmentsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Create the viewModel and adapter
        val appointmentsViewModel: AppointmentsViewModel by viewModels()

        appointmentsAdapter = MyAppointmensAdapter(this, mutableListOf())

        appointmentsViewModel.liveAppointments.observe(this, Observer { appointments ->
            appointmentsAdapter.setAppointments(appointments)
        })

        appointmentsViewModel.getAppointments()

        // Inflate the recycler view
        var recyclerView = binding.myAppointmentsRecyclerView

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = appointmentsAdapter

//        FirestoreClass().getUserName()

        // Set the action button
        binding.btnShowPlaces.setOnClickListener {
            Log.d("My Appointments", "Show places button clicked")
            val intent = Intent(this, PlacesActivity::class.java)
            startActivity(intent)
        }
    }


    fun successRetrieveAppointemnts(list: ArrayList<Appointment>) {
        appointmentsAdapter.setAppointments(list)
    }
}
