package com.bachelor.appoint.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.appoint.R
import com.bachelor.appoint.databinding.CardEAppointmentBinding
import com.bachelor.appoint.model.Appointment
import java.util.ArrayList

class EventAppointmentsAdapter(
    private var appointments: MutableList<Appointment>,

    ) : RecyclerView.Adapter<EventAppointmentsAdapter.AppointmentsViewHolder>() {
    class AppointmentsViewHolder(val binding: CardEAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tv_user_name: TextView = binding.tvUserName
        val tv_startTime: TextView = binding.tvStartTime
        val iv_status: ImageView = binding.ivStatus

        fun bindView(appointment: Appointment) {
            tv_user_name.text = appointment.userName// TODO add username
            tv_startTime.text = appointment.startTime

            when (appointment.status) {
                "pending" -> iv_status.setImageResource(R.drawable.ic_baseline_hourglass_bottom_24)
                "accepted" -> iv_status.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
                "cancelled" -> iv_status.setImageResource(R.drawable.ic_outline_cancel_24)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentsViewHolder {

        return AppointmentsViewHolder(
            CardEAppointmentBinding.inflate(
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

    fun setAppointments(list: ArrayList<Appointment>) {
        this.appointments = list
        notifyDataSetChanged()
    }

    fun getItem(adapterPosition: Int): Appointment {
        return appointments[adapterPosition]
    }


}