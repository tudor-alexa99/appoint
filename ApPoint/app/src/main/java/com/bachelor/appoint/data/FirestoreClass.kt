package com.bachelor.appoint.data

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.bachelor.appoint.LoginActivity
import com.bachelor.appoint.RegisterActivity
import com.bachelor.appoint.model.User
import com.bachelor.appoint.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.net.ContentHandler

class FirestoreClass {

    private val firestoreAdapter = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){
        // Save the newly created user in the database

        // Collection = "users"
        firestoreAdapter.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                activity.userRegistrationSuccess()
            }
            .addOnFailureListener {
                e -> Log.e("Register User Firestore", e.toString())
            }
    }

    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if(currentUser != null)
            currentUserID = currentUser.uid
        return currentUserID
    }

    fun getUserDetails(activity: Activity) {
        firestoreAdapter.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object
                val user = document.toObject(User::class.java)!!

                // Use the shared preferences in order to keep the name of the logged in user
                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                // Key: logged_in_username
                // Val:
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    user.fullName
                )
                editor.apply()

                // Pass the result to the Login Activity
                when (activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                }
            }
    }
}
