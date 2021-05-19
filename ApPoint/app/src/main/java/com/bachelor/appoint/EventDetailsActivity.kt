package com.bachelor.appoint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.ActivityEventDetailsBinding
import com.bachelor.appoint.model.Event
import com.bachelor.appoint.ui.EventInformationFragment
import com.bachelor.appoint.utils.Constants

class EventDetailsActivity : AppCompatActivity() {
    private lateinit var eventID: String
    private lateinit var binding: ActivityEventDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        // Get the event id parameter
        eventID = intent.getStringExtra("b_id").toString()

        // Bind the activity
        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Set the observer for the current event and bind the view
        FirestoreClass().getEventDetails(eventID, this)

        // Set the floating action buttons
        setActionButtons()
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

    private fun setActionButtons() {
        var makeButton = binding.btnMakeAppointment
        var cancelButton = binding.btnCancelAppointment

        makeButton.setOnClickListener(View.OnClickListener {
            // TODO: Add call for "Create create appointment"

            // When clicked, hide it and show the other button
            makeButton.animate().alpha(0.0f)
            makeButton.visibility = View.GONE
            cancelButton.animate().alpha(1.0f)
            cancelButton.visibility  = View.VISIBLE
        })


        cancelButton.setOnClickListener(View.OnClickListener {

            // When clicked, hide it and show the other button
            cancelButton.animate().alpha(0.0f)
            cancelButton.visibility  = View.GONE
            makeButton.animate().alpha(1.0f)
            makeButton.visibility = View.VISIBLE
        })
    }
}