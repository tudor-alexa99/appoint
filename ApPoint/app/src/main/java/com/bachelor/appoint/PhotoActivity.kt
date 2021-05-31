package com.bachelor.appoint

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.ActivityPhotoUserBinding
import com.bachelor.appoint.helpers.GlideLoader
import com.bachelor.appoint.utils.Constants
import uk.co.senab.photoview.PhotoViewAttacher
import java.io.IOException

class PhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoUserBinding
    private var selectedImageUri: Uri? = null
    private lateinit var retrievedImageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setImage()

        setButtons()
    }

    private fun setImage() {
        // look for the image inside firestore. If an image associated to the user exists, display it
        // else, show the placeholder
        FirestoreClass().retrieveUserImage(activity = this)
    }

    private fun setButtons() {
        setUploadPhotoButton()
    }

    private fun setUploadPhotoButton() {
        var button = binding.btnUploadFromGallery

        button.setOnClickListener {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )

            startActivityForResult(galleryIntent, Constants.PICK_IMAGE_REQUEST_CODE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null)
                    try {
                        selectedImageUri = data.data!!
                        if (selectedImageUri != null) {
                            GlideLoader(this).loadUserPicture(selectedImageUri!!, binding.ivPhoto)
                            enableSaveButton()
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@PhotoActivity,
                            "Image selection failed",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
            }
        }
    }

    private fun enableSaveButton() {
        binding.rgDoseNumber.visibility = View.VISIBLE
        binding.btnFloatingSave.visibility = View.VISIBLE

        Toast.makeText(
            this,
            "Your image was uploaded successfully",
            Toast.LENGTH_SHORT
        ).show()


        binding.btnFloatingSave.setOnClickListener {
            if (selectedImageUri != null)
                FirestoreClass().uploadImageToCloudStorage(this, selectedImageUri)
        }
    }

    fun imageUpdateUserSuccess() {
        Toast.makeText(
            this,
            "Upload image to user profile was successful",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun findImageSuccess(imageUri: String) {
        GlideLoader(this).loadUserPicture(imageUri.toUri(), binding.ivPhoto)
    }

    fun findImageFailure() {
        binding.ivPhoto.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24)
    }

    fun imageUploadSuccess(imageURL: String) {
        retrievedImageUrl = imageURL

        // Get the selected dose number
        var doseNumber: Int = 0
        if (binding.rbDose1.isChecked)
            doseNumber = 1
        else if (binding.rbDose2.isChecked)
            doseNumber = 2

        FirestoreClass().uploadImageToUserCollection(imageURL, this, doseNumber)
        finish()

    }
}
