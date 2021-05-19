package com.bachelor.appoint.adapters

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.appoint.EventDetailsActivity
import com.bachelor.appoint.R
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.CardPlaceBinding
import com.bachelor.appoint.model.Event
import kotlin.collections.ArrayList


class PlacesAdapter(
    private val context: Context,
    private val list: ArrayList<Event>,

    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class PlacesViewHolder(context: Context, binding: CardPlaceBinding) :
        RecyclerView.ViewHolder(binding.root), DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
        val parentContext: Context = context
        private lateinit var event: Event

        var _time = ""
        var _date = ""
        var day = 0
        var month = 0
        var year = 0
        var hour = 0
        var minute = 0

        var savedDay = 0
        var savedMonth = 0
        var savedYear = 0
        var savedHour = 0
        var savedMinute = 0

        val tv_title: TextView = binding.tvPlaceCardTitle
        val tv_address: TextView = binding.tvAddress
        val tv_phone: TextView = binding.tvEventPhoneNo
        val tv_type: TextView = binding.tvEventType
        val btn_card: LinearLayout = binding.cardEvent

        fun bindView(event: Event) {
            this.event = event

            tv_title.text = event.name
            tv_address.text = event.location
            tv_phone.text = event.phoneNumber
            tv_type.text = event.type

            bindCardButton(btn_card, parentContext, event)
        }

        private fun bindCardButton(btnCard: LinearLayout, context: Context, event: Event) {

            btnCard.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.quick_actions))
                    .setCancelable(true)
                    .setPositiveButton(context.getString(R.string.confirm)) { dialog, id ->
//                        TODO: AddAppointment
                        Log.i("Places Alert", "Confirm clicked")
                        pickDate()
                        Log.d("Date time", "${day}/${month}/${year}  ${hour}:${minute}")
                    }
                    .setNeutralButton(context.getString(R.string.more_info)) { dialog, id ->
                        val intent = Intent(this.parentContext, EventDetailsActivity::class.java)
                        // Set the parameters for the next activity
                        intent.putExtra("b_id", event.id)
                        this.parentContext.startActivity(intent)

                    }
                    .setNegativeButton(context.getString(R.string.cancel)) { dialog, id ->
                        dialog.dismiss()
                    }
                    .setMessage("Create a reservation for this place or get more details!")
                val alertDialog = builder.create()
                alertDialog.show()
            }
        }

        private fun pickDate() {
            pickDateTimeCalendar()
            DatePickerDialog(parentContext, this, year, month, day).show()
        }

        private fun pickDateTimeCalendar() {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            hour = calendar.get(Calendar.HOUR)
            minute = calendar.get(Calendar.MINUTE)
        }

        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            savedDay = dayOfMonth
            savedMonth = month
            savedYear = year

            TimePickerDialog(parentContext, this, hour, minute, true).show()
        }

        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            savedHour = hourOfDay
            savedMinute = minute
            _date = "${savedDay}/${savedMonth + 1}/${savedYear}"
            _time = "${savedHour}:${savedMinute}"

            FirestoreClass().getUserName(this)
        }

        fun successRetrieveUserName(userName: String) {
            FirestoreClass().addAppointment(_time, _date, event.id, event.name, userName)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlacesViewHolder(
            context,
            CardPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PlacesViewHolder) {
            holder.bindView(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}