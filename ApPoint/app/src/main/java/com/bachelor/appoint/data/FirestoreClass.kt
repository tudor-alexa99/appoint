package com.bachelor.appoint.data

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.bachelor.appoint.BusinessActivity
import com.bachelor.appoint.LoginActivity
import com.bachelor.appoint.RegisterActivity
import com.bachelor.appoint.model.Appointment
import com.bachelor.appoint.model.Business
import com.bachelor.appoint.model.User
import com.bachelor.appoint.utils.Constants
import com.bachelor.appoint.viewModel.AppointmentsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val firestoreAdapter = FirebaseFirestore.getInstance()
    private lateinit var currentUserID: String

//    Login and Register

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        // Save the newly created user in the database

        // Collection = "users"
        firestoreAdapter.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Register User Firestore", e.toString())
            }
    }

    fun getCurrentUserID(): String {
        // Check if it is already saved
        if (this::currentUserID.isInitialized)
            return currentUserID

        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        if (currentUser != null)
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

    //    Appointments
    fun addAppointment(
        viewModel: AppointmentsViewModel,
        startTime: String,
        businessId: String,
        businessName: String
    ) {

        // Create the model object
        val appointment =
            Appointment("", getCurrentUserID(), businessId, startTime, businessName, false)

        // Collection = appointments

        firestoreAdapter.collection(Constants.APPOINTMENTS)
            .add(appointment)
            .addOnSuccessListener { documentReference ->
                // Add the id field
                documentReference.update("id", documentReference.id)
                print("Add appointment with id + ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("Add appointment", e.toString())
            }
    }

    fun retrieveUserAppointments(viewModel: AppointmentsViewModel) {
//        Method that retrieves all the appointments of the current user

        firestoreAdapter.collection(Constants.APPOINTMENTS)
            .whereEqualTo("u_id", getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.d(viewModel.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<Appointment> = ArrayList()

                for (d in document.documents) {

                    val appointment = d.toObject(Appointment::class.java)!!
                    list.add(appointment)
                }

                when (viewModel) {
                    is AppointmentsViewModel -> {
                        viewModel.successRetrieveAppointments(list)
                    }
                }
            }
            .addOnFailureListener {
                Log.e(viewModel.javaClass.simpleName, "Error while retrieving the appointments")
            }

    }
//    Business

    fun addBusiness(
        activity: BusinessActivity,
        name: String,
        address: String,
        phone: String,
        type: String
    ) {
        // Test function for adding a business

        val business = Business(getCurrentUserID(), name, address, phone, type)

        // Collection = "business"
        firestoreAdapter.collection(Constants.BUSINESSES)
            .add(business)
            .addOnSuccessListener { documentReference ->
                // Add the id
                documentReference.update("id", documentReference.id)

                // Add the business id to the user
                firestoreAdapter.collection(Constants.USERS)
                    .document(getCurrentUserID())
                    .collection(Constants.BUSINESSES)
                    .document(documentReference.id)
                    .set(mapOf(documentReference.id to business.name), SetOptions.merge())
                    .addOnSuccessListener {
                        activity.addBusinessSuccess()
                    }
                    .addOnFailureListener { e ->
                        Log.e("Add Business", e.toString())
                    }
            }
    }

    fun retrieveBusinesses(activity: Activity) {
        firestoreAdapter.collection(Constants.BUSINESSES)
            .whereEqualTo(Constants.ADMIN_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.d(activity.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<Business> = ArrayList()

                for (d in document.documents) {

                    val business = d.toObject(Business::class.java)!!
                    list.add(business)
                }

                when (activity) {
                    is BusinessActivity -> {
                        activity.successRetrieveBusinesses(list)
                    }
                }
            }
            .addOnFailureListener {
                Log.e(activity.javaClass.simpleName, "Error while retrieving the businesses")
            }

    }
}
