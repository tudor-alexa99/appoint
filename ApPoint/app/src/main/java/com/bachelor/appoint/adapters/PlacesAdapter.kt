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
        RecyclerView.ViewHolder(binding.root) {
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
                val intent = Intent(this.parentContext, EventDetailsActivity::class.java)
                // Set the parameters for the next activity
                intent.putExtra("event", event)
                this.parentContext.startActivity(intent)
            }
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