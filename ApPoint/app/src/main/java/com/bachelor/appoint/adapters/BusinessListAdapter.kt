package com.bachelor.appoint.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.appoint.databinding.CardBusinessBinding
import com.bachelor.appoint.model.Business

open class BusinessListAdapter(
    private val context: Context,
    private val list: ArrayList<Business>,

    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var currentPosition: Int = 0

    class BusinessViewHolder(val binding: CardBusinessBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tv_title: TextView = binding.tvBusinessCardTitle
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
        return BusinessViewHolder(
            CardBusinessBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BusinessViewHolder) {
            holder.bindView(list[position])
        }
    }

    fun getItemPosition(itemId: String): Int {
        for (i in 0 until list.size) {
            if (list.get(i).id.equals(itemId)) {
                return i
            }
        }
        return -1
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getCurrentPosition(): Int {
        return currentPosition
    }

    fun nextPosition() {
        if (currentPosition < list.size -1)
            currentPosition += 1
    }
    fun previousPosition() {
        if (currentPosition > 0)
            currentPosition -= 1
    }

}