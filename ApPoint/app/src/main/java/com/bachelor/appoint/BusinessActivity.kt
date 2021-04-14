package com.bachelor.appoint

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.ActivityBusinessBinding
import com.bachelor.appoint.databinding.FragmentAddBusinessAlertBinding
import com.bachelor.appoint.ui.AddBusinessAlertFragment

class BusinessActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBusinessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind the activity to the view
        binding = ActivityBusinessBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Add business button
        binding.btnAddBusiness.setOnClickListener {
            print("Button clicked")
            Log.d("Add business button clicked", "Business Activity")
            openAlertDialog()
        }

    }

    fun openAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add new business")
        val alertBinding = FragmentAddBusinessAlertBinding.inflate(layoutInflater)
        val alertView =  alertBinding.root
        builder.setView(alertView)

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            val name: String = alertBinding.etBusinessName.text.toString()
            val address: String = alertBinding.etBusinessAddress.text.toString()
            val phoneNumber: String = alertBinding.etPhone.text.toString()
            val type: String = alertBinding.etType.text.toString()

            FirestoreClass().addBusiness(this, name, address, phoneNumber, type)
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, which ->
            Toast.makeText(applicationContext,
                android.R.string.cancel, Toast.LENGTH_SHORT).show()
        }

        builder.show()
    }

    fun addBusinessSuccess() {
        Toast.makeText(applicationContext,
            "Business added!", Toast.LENGTH_LONG).show()
    }
}