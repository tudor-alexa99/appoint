package com.bachelor.appoint.utils

import android.app.Activity
import android.net.Uri
import android.webkit.MimeTypeMap

object Constants {
    const val FULL_NAME: String = "fullName"
    const val APPOINTMENTS: String = "appointments"
    const val USERS: String = "users"
    const val PREFERENCES: String = "MyPreferences"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val ADMIN_ID: String = "adminId"
    const val EVENT: String = "event"
    const val EVENTS: String = "events"
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val IMAGE: String = "image"

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}