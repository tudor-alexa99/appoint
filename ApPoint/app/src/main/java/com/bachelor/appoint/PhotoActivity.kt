package com.bachelor.appoint

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.ActivityPhotoBinding
import com.bachelor.appoint.databinding.ActivityReportBinding
import com.bachelor.appoint.helpers.GlideLoader
import com.bachelor.appoint.utils.Constants
import java.io.IOException

class PhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoBinding
    private var selectedImageUri: Uri? = null
    private lateinit var retrievedImageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoBinding.inflate(layoutInflater)
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
        setTakePhotoButton()
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

    private fun setTakePhotoButton() {
        var button = binding.btnTakePhoto

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null)
                    try {
                        selectedImageUri = data.data!!

                        if (selectedImageUri != null)
                            FirestoreClass().uploadImageToCloudStorage(this, selectedImageUri)

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

    fun imageUploadSuccess(imageURL: String) {
        Toast.makeText(
            this,
            "Your image was uploaded successfully",
            Toast.LENGTH_SHORT
        ).show()
        retrievedImageUrl = imageURL
        binding.btnFloatingSave.visibility = View.VISIBLE
        binding.rgDoseNumber.visibility = View.VISIBLE
        binding.btnFloatingSave.setOnClickListener {
            FirestoreClass().uploadImageToUserCollection(imageURL, this)
            finish()
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
}
