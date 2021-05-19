package com.bachelor.appoint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.ActivityBusinessDetailsBinding
import com.bachelor.appoint.databinding.ActivityPlacesBinding
import com.bachelor.appoint.model.Business
import com.bachelor.appoint.ui.BusinessInformatioFragment
import com.bachelor.appoint.utils.Constants

class BusinessDetailsActivity : AppCompatActivity() {
    private lateinit var businessID: String
    private lateinit var binding: ActivityBusinessDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_details)

        // Get the business id parameter
        businessID = intent.getStringExtra("b_id").toString()

        // Bind the activity
        binding = ActivityBusinessDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Set the observer for the current business and bind the view
        FirestoreClass().getBusinessDetails(businessID, this)

        // Set the floating action buttons
        setActionButtons()
    }

    fun successObserveBusiness(business: Business) {
        // Once the business is retrieved, update the layout for the activity

        // Start the fragment
        val manager = supportFragmentManager

        val bundle = Bundle()
        bundle.putParcelable(Constants.BUSINESS, business)

        val infoFragment = BusinessInformatioFragment()
        infoFragment.arguments = bundle

        // initiate the transaction that replaces the fragments
        val transaction = manager.beginTransaction().setCustomAnimations(
            android.R.animator.fade_in,
            android.R.animator.fade_out
        )
        transaction.replace(R.id.fr_business_details_fullscreen, infoFragment)
        transaction.commit()

    }

    private fun setActionButtons() {
        var makeButton = binding.btnMakeAppointment
        var cancelButton = binding.btnCancelAppointment

        makeButton.setOnClickListener(View.OnClickListener {
            // TODO: Add call for "Create create appointment"

            // When clicked, hide it and show the other button
            makeButton.visibility = View.GONE
            cancelButton.visibility  = View.VISIBLE
        })


        cancelButton.setOnClickListener(View.OnClickListener {

            // When clicked, hide it and show the other button
            cancelButton.visibility  = View.GONE
            makeButton.visibility = View.VISIBLE
        })
    }
}