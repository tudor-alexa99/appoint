package com.bachelor.appoint.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.appoint.databinding.CardBusinessBinding
import com.bachelor.appoint.databinding.CardPlaceBinding
import com.bachelor.appoint.model.Business

class PlacesAdapter(
    private val context: Context,
    private val list: ArrayList<Business>,

    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class PlacesViewHolder(val binding: CardPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tv_title: TextView = binding.tvPlaceCardTitle
        val tv_address: TextView = binding.tvAddress
        val tv_phone: TextView = binding.tvBusinessPhoneNo
        val tv_type: TextView = binding.tvBusinessType

        fun bindView(business: Business) {
            tv_title.text = business.name
            tv_address.text = business.location
            tv_phone.text = business.phoneNumber
            tv_type.text = business.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlacesViewHolder(
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