package com.bachelor.appoint.helpers

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bachelor.appoint.R
import com.bumptech.glide.Glide
import java.io.IOException

class GlideLoader(val context: Context) {

    fun loadUserPicture(imageUri: Uri, imageView: ImageView) {
        try {
            Glide
                .with(context)
                .load(imageUri)
                .placeholder(R.drawable.ic_baseline_add_photo_alternate_24)
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}