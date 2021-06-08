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
import org.w3c.dom.Text

class MyAppointmensAdapter(
    private val context: Context,
    private var appointments: MutableList<Appointment>,

    ) : RecyclerView.Adapter<MyAppointmensAdapter.AppointmentsViewHolder>() {
    class AppointmentsViewHolder(val binding: CardAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tv_place: TextView = binding.root.findViewById(R.id.tv_event_name)
        val tv_startTime: TextView = binding.root.findViewById(R.id.tv_time)
        val tv_date: TextView = binding.root.findViewById(R.id.tv_date)
        val iv_status: ImageView = binding.root.findViewById(R.id.iv_status)

        fun bindView(appointment: Appointment) {
            tv_place.text = appointment.event
            tv_startTime.text = appointment.startTime
            tv_date.text = appointment.date

            when (appointment.status) {
                "waiting" -> iv_status.setImageResource(R.drawable.ic_baseline_hourglass_bottom_24)
                "accepted" -> iv_status.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
                "cancelled" -> iv_status.setImageResource(R.drawable.ic_outline_cancel_24)
            }
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