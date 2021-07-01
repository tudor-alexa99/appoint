package com.bachelor.appoint.ui

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.appoint.R
import com.bachelor.appoint.adapters.EventAppointmentsAdapter
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.FragmentAppointmentsListBinding
import com.bachelor.appoint.model.Appointment
import com.bachelor.appoint.model.Event
import com.bachelor.appoint.utils.Constants
import java.util.*


class AppointmentsListFragment : Fragment() {
    private var event: Event? = null
    private lateinit var binding: FragmentAppointmentsListBinding
    private lateinit var appointmentsList: ArrayList<Appointment>
    private lateinit var appointmentsAdapter: EventAppointmentsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            event = it.getParcelable(Constants.EVENT)
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

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_event_appointments)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        appointmentsAdapter = EventAppointmentsAdapter(mutableListOf(), requireContext())
        recyclerView.adapter = appointmentsAdapter

        itemTouchHelper.attachToRecyclerView(recyclerView)


        getAppointmentsList()
        return view
    }

    private fun getAppointmentsList() {
        FirestoreClass().retrieveEventAppointments(this, event!!.id)
    }

    fun successRetrieveAppointments(list: ArrayList<Appointment>) {
        appointmentsList = list
        appointmentsAdapter.setAppointments(list)
    }

    var itemTouchHelper = ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                // move item in `fromPos` to `toPos` in adapter.
                return true // true if moved, false otherwise

            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                // select the current appointment you're swiping on
                Log.d("Swipe direction", direction.toString())
                var appointment: Appointment =
                    appointmentsAdapter.getItem(viewHolder.adapterPosition)


                // On swipe left, cancel the appointment. On swipe right, accept it
                when (direction) {
                    8 -> FirestoreClass().acceptAppointment(appointment.id)
                    4 -> FirestoreClass().denyAppointment(appointment.id)
                }
                appointmentsAdapter.notifyDataSetChanged()
//                appointmentsAdapter.notifyItemChanged(viewHolder.adapterPosition)

            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView

                val dRight = ContextCompat.getDrawable(context!!, R.drawable.bg_swipe_item)
                dRight!!.setBounds(
                    itemView.left,
                    itemView.top,
                    dX.toInt(),
                    itemView.bottom
                )

                dRight.draw(c)
                val dLeft = ContextCompat.getDrawable(context!!, R.drawable.bg_swipe_item)
                dLeft!!.setBounds(
                    dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )

                dLeft.draw(c)
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX / 5,
                    dY,
                    actionState,
                    isCurrentlyActive
                )


                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX / 5,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            override fun getAnimationDuration(
                recyclerView: RecyclerView,
                animationType: Int,
                animateDx: Float,
                animateDy: Float
            ): Long {
                return 400
            }
        }
    )

    companion object {
        @JvmStatic
        fun newInstance(event: Event) =
            AppointmentsListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.EVENT, event)
                }
            }
    }
}
