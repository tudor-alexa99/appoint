package com.bachelor.appoint.adapters

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.appoint.PhotoAppointmentActivity
import com.bachelor.appoint.R
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.CardEAppointmentBinding
import com.bachelor.appoint.model.Appointment
import com.bachelor.appoint.model.User
import java.util.ArrayList

class EventAppointmentsAdapter(
    private var appointments: MutableList<Appointment>,
    private var context: Context,
) : RecyclerView.Adapter<EventAppointmentsAdapter.AppointmentsViewHolder>() {


    class AppointmentsViewHolder(val binding: CardEAppointmentBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        val tv_user_name: TextView = binding.tvUserName
        val tv_startTime: TextView = binding.tvStatusValue
        val iv_status: ImageView = binding.ivStatus

        fun bindView(appointment: Appointment) {
            tv_user_name.text = appointment.userName
            tv_startTime.text = appointment.startTime
            binding.tvStatusValue.text = appointment.status

//            when (appointment.status) {
//                "pending" -> iv_status.setImageResource(R.drawable.ic_baseline_hourglass_bottom_24)
//                "accepted" -> iv_status.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
//                "cancelled" -> iv_status.setImageResource(R.drawable.ic_outline_cancel_24)
//            }

            FirestoreClass().getUserVaccinationDetails(appointment.u_id, holder = this)


            itemView.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder
                    .setPositiveButton("Confirm") { _, _ ->
                        Log.d("Yes button", "Clicked")
                        FirestoreClass().acceptAppointment(appointment.id)
                    }
                    .setNegativeButton("Reject") { _, _ ->
                        FirestoreClass().denyAppointment(appointment.id)
                    }
                    .setNeutralButton("Details") { _, _ ->
                        Log.d(TAG, "Show user image")
                        // Launch the fullscreen photo activity

                        val intent = Intent(context, PhotoAppointmentActivity::class.java)

                        intent.putExtra("u_id", appointment.u_id)
                        intent.putExtra("a_id", appointment.id)

                        context.startActivity(intent)

                    }
                    .setTitle(appointment.userName)
                    .setMessage("Get more details about the user?")

                builder.show()


//                FirestoreClass().getUserDetails(adaper = this)
            }
        }

        fun setUserDetails(user: User) {
            binding.tvDoseNumber.text = user.doseNumber.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentsViewHolder {

        return AppointmentsViewHolder(
            CardEAppointmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            context
        )
    }


    override fun onBindViewHolder(holder: AppointmentsViewHolder, position: Int) {
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

    fun successGetUserInfo(user: User, holder: AppointmentsViewHolder) {
        holder.setUserDetails(user)
    }

}