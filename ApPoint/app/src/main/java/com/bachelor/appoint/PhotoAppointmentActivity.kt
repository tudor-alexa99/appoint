package com.bachelor.appoint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.ActivityPhotoAppointmentBinding
import com.bachelor.appoint.helpers.GlideLoader

class PhotoAppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoAppointmentBinding
    private lateinit var imageURL: String
    private lateinit var userId: String
    private lateinit var appointmentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get the activity parameters
        getActivityParams()

        // get the image for the user
        getUserImage()

        // set the action for the buttons
        setButtonsAction()
    }

    private fun setButtonsAction() {
        // accept button
        binding.btnFloatingAccept.setOnClickListener {
            FirestoreClass().acceptAppointment(appointmentId)
            finish()
        }

        binding.btnFloatingReject.setOnClickListener {
            FirestoreClass().denyAppointment(appointmentId)
            finish()
        }
    }

    private fun getUserImage() {
        FirestoreClass().retrieveUserImage(userId, this)
    }

    private fun getActivityParams() {
        userId = intent.getStringExtra("u_id").toString()
        appointmentId = intent.getStringExtra("a_id").toString()
    }

    fun setUserImage(imageURL: String) {
        this.imageURL = imageURL

        // set the user image
        GlideLoader(this).loadUserPicture(imageURL.toUri(), binding.ivPhoto)
    }
}
