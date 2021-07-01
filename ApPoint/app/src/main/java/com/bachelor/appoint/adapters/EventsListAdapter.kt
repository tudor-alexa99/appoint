package com.bachelor.appoint.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.appoint.databinding.CardEventBinding
import com.bachelor.appoint.model.Event

open class EventsListAdapter(
    private val list: ArrayList<Event>,

    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class EventViewHolder(val binding: CardEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tv_title: TextView = binding.tvEventCardTitle
        val tv_address: TextView = binding.tvAddress
        val tv_phone: TextView = binding.tvEventPhoneNo
        val tv_type: TextView = binding.tvEventType

        fun bindView(event: Event) {
            tv_title.text = event.name
            tv_address.text = event.location
            tv_phone.text = event.phoneNumber
            tv_type.text = event.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EventViewHolder(
            CardEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EventViewHolder) {
            holder.bindView(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getItem(adapterPosition: Int): Event {
        return list[adapterPosition]
    }
}
