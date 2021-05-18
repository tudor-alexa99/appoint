package com.bachelor.appoint.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.appoint.R
import com.bachelor.appoint.databinding.CardAppointmentBinding
import com.bachelor.appoint.model.Appointment

class MyAppointmensAdapter(
    private val context: Context,
    private var appointments: MutableList<Appointment>,

    ) : RecyclerView.Adapter<MyAppointmensAdapter.AppointmentsViewHolder>() {
    class AppointmentsViewHolder(val binding: CardAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tv_place: TextView = binding.root.findViewById(R.id.tv_user_name)
        val tv_startTime: TextView = binding.root.findViewById(R.id.tv_startTime)
        val iv_status: ImageView = binding.root.findViewById(R.id.iv_status)

        fun bindView(appointment: Appointment) {
            tv_place.text = appointment.business
            tv_startTime.text = appointment.startTime

            if (appointment.status == "pending")
                iv_status.setImageResource(R.drawable.ic_baseline_hourglass_bottom_24)
            else if (appointment.status == "accepted")
                iv_status.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
            else if(appointment.status == "cancelled")
                iv_status.setImageResource(R.drawable.ic_outline_cancel_24)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentsViewHolder {
        return AppointmentsViewHolder(
            CardAppointmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AppointmentsViewHolder, position: Int) {
        holder.binding.appointmentCard.setOnClickListener { Log.d("tag", "clicked") }
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