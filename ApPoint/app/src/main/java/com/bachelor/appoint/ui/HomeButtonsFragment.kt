package com.bachelor.appoint.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.bachelor.appoint.EventsActivity
import com.bachelor.appoint.MyAppointmentsActivity
import com.bachelor.appoint.R
import com.bachelor.appoint.ReportActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeButtonsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeButtonsFragment : Fragment() {
    // TODO: Rename and change types of parameters
//    var appointmentsButton = null
//    var eventButton = null
//    var reportButton = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_home_buttons, container, false)
        var appointmentsButton = view.findViewById(R.id.my_appointments_button) as Button
        var eventButton = view.findViewById(R.id.event_button) as Button
        var reportButton = view.findViewById(R.id.report_button) as Button


        appointmentsButton.setOnClickListener { view -> navigateMyAppointmens(view) }
        eventButton.setOnClickListener { view -> navigateEvent(view) }
        reportButton.setOnClickListener { view -> navigateReport(view) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeButtonsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HomeButtonsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    fun navigateMyAppointmens(view: View) {
        val intent = Intent(activity, MyAppointmentsActivity::class.java)
        activity?.startActivity(intent)
//        activity?.finish()
    }

    fun navigateEvent(view: View) {
        val intent = Intent(activity, EventsActivity::class.java)
        activity?.startActivity(intent)
//        activity?.finish()
    }

    fun navigateReport(view: View) {
        val intent = Intent(activity, ReportActivity::class.java)
        activity?.startActivity(intent)
//        activity?.finish()
    }
}
