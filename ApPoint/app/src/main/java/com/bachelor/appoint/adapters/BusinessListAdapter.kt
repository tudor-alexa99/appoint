package com.bachelor.appoint.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.appoint.R
import com.bachelor.appoint.model.Business
import kotlin.contracts.Returns

open class BusinessListAdapter(
    private val context: Context,
    private val list: ArrayList<Business>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BusinessViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.card_business,
                parent,
                false
            )
        )
     }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is BusinessViewHolder){

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class BusinessViewHolder(view: View): RecyclerView.ViewHolder(view)
}