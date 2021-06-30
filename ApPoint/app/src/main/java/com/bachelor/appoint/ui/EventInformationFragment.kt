package com.bachelor.appoint.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bachelor.appoint.R
import com.bachelor.appoint.databinding.FragmentEventInformationBinding
import com.bachelor.appoint.model.Event
import com.bachelor.appoint.utils.Constants
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private const val EVENT = "event"


class EventInformationFragment : Fragment() {

    private var event: Event? = null
    private lateinit var binding: FragmentEventInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            event = it.getParcelable(Constants.EVENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (event != null) {
            Log.d("Inside fragment", event!!.name)
        }
        return inflater.inflate(R.layout.fragment_event_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEventInformationBinding.bind(view)
        if (event != null)
            setFragmentView(event!!)
        else
            setEmptyFragmentView()
    }

    private fun setEmptyFragmentView() {
        binding.tvCompanyName.text =
            "Swipe up for event details, swipe down for appointments list"
        binding.clBDetailsBody.visibility = View.GONE
    }

    private fun setFragmentView(currentEvent: Event) {
        // set the name
        binding.tvCompanyName.text = currentEvent.name

        // set the risk progressBar with an animation
        event?.let { //binding.statsProgressbar.setProgress(it.estimatedRisk, true)
            if (event!!.estimatedRisk < 33)
                ObjectAnimator.ofInt(binding.statsProgressbarLow, "progress", it.estimatedRisk)
                    .setDuration(400)
                    .start()
            else if (event!!.estimatedRisk < 66)
                ObjectAnimator.ofInt(binding.statsProgressbarMedium, "progress", it.estimatedRisk)
                    .setDuration(900)
                    .start()
            else
                ObjectAnimator.ofInt(binding.statsProgressbarHigh, "progress", it.estimatedRisk)
                    .setDuration(1200)
                    .start()
        }

        // set the number inside the pie chart
        binding.tvScore.text = event!!.estimatedRisk.toString()

        // set the event type
        binding.tvBiTypeVal.text = event!!.type

        // set the seats number
        binding.tvBiSeatsVal.text = event!!.seatsNumber.toString()

        // compute and round the average area for each user
        binding.tvBiUserAreaVal.text =
            ("%.1f".format((event!!.estimatedSurface.toFloat() / event!!.seatsNumber))) + " m²"

        // format and set the average time
        val time = LocalTime.parse(event!!.duration, DateTimeFormatter.ofPattern("H:mm"))
        val timeValue = time.hour.toString() + "hr " + time.minute.toString() + "min"
        binding.tvBiAvgTimeSpentVal.text = timeValue

        // set the address
        binding.tvBiAddressVal.text = event!!.location

        // set the time
        binding.tvEventTime.text = "Time: " + event!!.time

        // set the date
        binding.tvEventDate.text = "Date: " + event!!.date

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(event: Event) =
            EventInformationFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EVENT, event)
                }
            }
    }
}