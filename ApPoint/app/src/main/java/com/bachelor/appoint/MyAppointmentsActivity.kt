package com.bachelor.appoint

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.appoint.data.appointmentsList
import com.bachelor.appoint.ui.MyAppointmensAdapter

class MyAppointmentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_appointments)

        val appointmensAdapter = MyAppointmensAdapter(this, appointmentsList)

        var recyclerView = findViewById<RecyclerView>(R.id.my_appointments_recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = appointmensAdapter
    }
}