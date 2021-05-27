package com.bachelor.appoint

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.ActivityPhotoBinding
import com.bachelor.appoint.databinding.ActivityReportBinding
import com.bachelor.appoint.helpers.GlideLoader
import com.bachelor.appoint.utils.Constants
import java.io.IOException

class PhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoBinding
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtons()
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

                        GlideLoader(this).loadUserPicture(selectedImageUri!!, binding.ivPhoto)

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


    }
}
