package com.bachelor.appoint.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.appoint.R
import com.bachelor.appoint.adapters.BusinessAppointmentsAdapter
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.FragmentAppointmentsListBinding
import com.bachelor.appoint.model.Appointment
import com.bachelor.appoint.model.Business
import com.bachelor.appoint.utils.Constants
import java.util.ArrayList

class AppointmentsListFragment : Fragment() {
    private var business: Business? = null
    private lateinit var binding: FragmentAppointmentsListBinding
    private lateinit var appointmentsList: ArrayList<Appointment>
    private lateinit var appointmentsAdapter: BusinessAppointmentsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            business = it.getParcelable(Constants.BUSINESS)
        }
        binding = FragmentAppointmentsListBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_appointments_list, container, false)
        binding = FragmentAppointmentsListBinding.inflate(layoutInflater)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_business_appointments)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        appointmentsAdapter = BusinessAppointmentsAdapter(mutableListOf())
        recyclerView.adapter = appointmentsAdapter

        getAppointmentsList()
        return view
    }

    private fun getAppointmentsList() {
        FirestoreClass().retrieveBusinessAppointments(this, business!!.id)
    }

    fun successRetrieveAppointments(list: ArrayList<Appointment>) {
        appointmentsList = list
        appointmentsAdapter.setAppointments(list)
    }

    companion object {
        @JvmStatic
        fun newInstance(business: Business) =
            AppointmentsListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.BUSINESS, business)
                }
            }
    }
}