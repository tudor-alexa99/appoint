package com.bachelor.appoint.data

import android.util.Log
import com.bachelor.appoint.RegisterActivity
import com.bachelor.appoint.model.User
import com.bachelor.appoint.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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
}
