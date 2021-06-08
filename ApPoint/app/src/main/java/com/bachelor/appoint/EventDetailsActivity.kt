package com.bachelor.appoint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.ActivityEventDetailsBinding
import com.bachelor.appoint.model.Event
import com.bachelor.appoint.ui.EventInformationFragment
import com.bachelor.appoint.utils.Constants

class EventDetailsActivity : AppCompatActivity() {
    private lateinit var event: Event
    private lateinit var binding: ActivityEventDetailsBinding
    private lateinit var userName: String
    private var existingAppointment: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        // Set the user name
        FirestoreClass().getUserName(this)

        // Get the event id parameter
        event = intent.getParcelableExtra<Event>("event")!!

        // Check if there is an existing appointment for this event
        FirestoreClass().checkForExistingAppointment(event.id, this)

        // Bind the activity
        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Set the observer for the current event and bind the view
        FirestoreClass().getEventDetails(event.id, this)

        // Set the floating action buttons
//        setActionButtons()
        // The action buttons will only be set after the check for an appointment is made
    }

    fun successObserveEvent(event: Event) {
        // Once the event is retrieved, update the layout for the activity

        // Start the fragment
        val manager = supportFragmentManager

        val bundle = Bundle()
        bundle.putParcelable(Constants.EVENT, event)

        val infoFragment = EventInformationFragment()
        infoFragment.arguments = bundle

        // initiate the transaction that replaces the fragments
        val transaction = manager.beginTransaction().setCustomAnimations(
            android.R.animator.fade_in,
            android.R.animator.fade_out
        )
        transaction.replace(R.id.fr_event_details_fullscreen, infoFragment)
        transaction.commit()

    }

    fun successRetrieveUserName(userName: String) {
        this.userName = userName
    }

    private fun setActionButtons() {
        var makeButton = binding.btnMakeAppointment
        var cancelButton = binding.btnCancelAppointment

        // check if there is an appointment already made:
        Log.d("Existing appointment", existingAppointment.toString())

        // If true, set the button to cancel, else set it to create
        when (existingAppointment) {
            true -> {
                makeButton.visibility = View.GONE
                cancelButton.visibility = View.VISIBLE
            }

            false -> {
                cancelButton.visibility = View.GONE
                makeButton.visibility = View.VISIBLE

            }

        }

        makeButton.setOnClickListener(View.OnClickListener {
            Log.i("Event", event.date)

            // Create an appointment for the given event
            FirestoreClass().addAppointment(event.time, event.date, event.id, event.name,event.type, userName, event.estimatedRisk)

            // When clicked, hide it and show the other button
            makeButton.animate().alpha(0.0f)
            makeButton.visibility = View.GONE
            cancelButton.animate().alpha(1.0f)
            cancelButton.visibility = View.VISIBLE
        })


        cancelButton.setOnClickListener(View.OnClickListener {

            // Delete the appointment for the given event
            FirestoreClass().deleteAppointment(event.id)

            // When clicked, hide it and show the other button
            cancelButton.animate().alpha(0.0f)
            cancelButton.visibility = View.GONE
            makeButton.animate().alpha(1.0f)
            makeButton.visibility = View.VISIBLE

        })
    }

    fun successCheckForAppointment(b: Boolean) {
        this.existingAppointment = b
        setActionButtons()
    }


}