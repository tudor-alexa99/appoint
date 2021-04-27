package com.bachelor.appoint.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.appoint.R
import com.bachelor.appoint.data.appointmentsList
import com.bachelor.appoint.databinding.CardAppointmentBinding
import com.bachelor.appoint.model.Appointment
import com.bachelor.appoint.viewModel.AppointmentsViewModel
import org.w3c.dom.Text

class MyAppointmensAdapter(
    private val context: Context,
    private var appointments: MutableList<Appointment>,

): RecyclerView.Adapter<MyAppointmensAdapter.AppointmentsViewHolder>() {
    class AppointmentsViewHolder(val binding: CardAppointmentBinding): RecyclerView.ViewHolder(binding.root) {
        val tv_place: TextView = binding.root.findViewById(R.id.tv_place)
        val tv_startTime: TextView = binding.root.findViewById(R.id.tv_startTime)

        fun bindView(appointment: Appointment) {
            tv_place.text = appointment.place
            tv_startTime.text = appointment.startTime
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentsViewHolder {
        return AppointmentsViewHolder(CardAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AppointmentsViewHolder, position: Int) {
        holder.binding.appointmentCard.setOnClickListener{Log.d("tag", "clicked")}
        holder.bindView(appointments[position])
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    fun setAppointments(appointments: List<Appointment>?) {
        this.appointments = appointments as MutableList<Appointment>
        notifyDataSetChanged()
    }

}