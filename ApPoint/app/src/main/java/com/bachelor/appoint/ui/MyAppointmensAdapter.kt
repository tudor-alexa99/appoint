package com.bachelor.appoint.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.appoint.data.appointmentsList
import com.bachelor.appoint.databinding.AppointmentsRowBinding
import com.bachelor.appoint.model.Appointment
import com.bachelor.appoint.viewModel.AppointmentsViewModel

class MyAppointmensAdapter(
    private val context: Context,
    private val appointments: MutableList<Appointment>,
): RecyclerView.Adapter<MyAppointmensAdapter.AppointmentsViewHolder>() {
    class AppointmentsViewHolder(val binding: AppointmentsRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentsViewHolder {
        return AppointmentsViewHolder(AppointmentsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AppointmentsViewHolder, position: Int) {
        holder.binding.appoitmentCard.setOnClickListener{Log.d("tag", "clicked")}

    }

    override fun getItemCount(): Int {
        return appointments.size
    }

}