package com.bachelor.appoint.adapters

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import android.app.AlertDialog
import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.appoint.R
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.CardPlaceBinding
import com.bachelor.appoint.model.Business
import kotlin.collections.ArrayList


class PlacesAdapter(
    private val context: Context,
    private val list: ArrayList<Business>,

    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class PlacesViewHolder(context: Context, binding: CardPlaceBinding) :
        RecyclerView.ViewHolder(binding.root), DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {
        val parentContext: Context = context
        private lateinit var business: Business

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
        val tv_phone: TextView = binding.tvBusinessPhoneNo
        val tv_type: TextView = binding.tvBusinessType
        val btn_card: LinearLayout = binding.cardBusiness

        fun bindView(business: Business) {
            this.business = business

            tv_title.text = business.name
            tv_address.text = business.location
            tv_phone.text = business.phoneNumber
            tv_type.text = business.type

            bindCardButton(btn_card, parentContext, business)
        }

        private fun bindCardButton(btnCard: LinearLayout, context: Context, business: Business) {

            btnCard.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.about_business))
                    .setCancelable(true)
                    .setPositiveButton(context.getString(R.string.confirm)) { dialog, id ->
//                        TODO: AddAppointment
                        Log.i("Places Alert", "Confirm clicked")
                        pickDate()
                        Log.d("Date time", "${day}/${month}/${year}  ${hour}:${minute}")
                    }
//                    .setNeutralButton(context.getString(R.string.choose_a_time)) { dialog, id ->
//                        pickDate()
//                    }
                    .setNegativeButton(context.getString(R.string.cancel)) { dialog, id ->
                        dialog.dismiss()
                    }
                    .setMessage("Create a reservation for this place!")
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
            _date = "${savedDay}/${savedMonth}/${savedYear}"
            _time = "${savedHour}:${savedMinute}"
            FirestoreClass().addAppointment(_time, _date, business.id, business.name)
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