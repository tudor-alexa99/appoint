package com.bachelor.appoint.data

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bachelor.appoint.*
import com.bachelor.appoint.adapters.EventAppointmentsAdapter
import com.bachelor.appoint.model.Appointment
import com.bachelor.appoint.model.Event
import com.bachelor.appoint.model.Risk
import com.bachelor.appoint.model.User
import com.bachelor.appoint.ui.AppointmentsListFragment
import com.bachelor.appoint.utils.Constants
import com.bachelor.appoint.viewModel.AppointmentsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

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

    private fun getCurrentUserID(): String {
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

    fun getUserDetails(activity: Activity? = null) {
        firestoreAdapter.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity!!.javaClass.simpleName, document.toString())

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
        startTime: String,
        date: String,
        eventId: String,
        eventName: String,
        eventType: String,
        userName: String,
        risk: Int
    ) {

        //Get the user name

        // Create the model object

        val appointment =
            Appointment(
                "",
                getCurrentUserID(),
                eventId,
                eventName,
                eventType,
                startTime,
                date,
                false,
                userName = userName,
                risk = risk
            )

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

    fun retrieveUserAppointments(viewModel: AppointmentsViewModel?, activity: Activity?) {
//        Method that retrieves all the appointments of the current user

        firestoreAdapter.collection(Constants.APPOINTMENTS)
            .whereEqualTo("u_id", getCurrentUserID())
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("Retrieve appointments", e.message.toString())
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val list: ArrayList<Appointment> = ArrayList()

                    for (d in snapshot.documents) {

                        val appointment = d.toObject(Appointment::class.java)!!
                        list.add(appointment)
                    }

                    when (viewModel) {
                        is AppointmentsViewModel -> {
                            viewModel.successRetrieveAppointments(list)
                        }
                    }

                    if (activity != null && activity is ReportActivity)
                        activity.successRetrieveUserAppointments(list)

                }
            }
    }

    fun retrieveEventAppointments(fragment: Fragment, eventId: String) {
//        Method that retrieves all the appointments of the current user

        firestoreAdapter.collection(Constants.APPOINTMENTS)
            .whereEqualTo("e_id", eventId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("Retrieve appointments", e.message.toString())
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val list: ArrayList<Appointment> = ArrayList()

                    for (d in snapshot.documents) {

                        val appointment = d.toObject(Appointment::class.java)!!
                        list.add(appointment)
                    }

                    when (fragment) {
                        is AppointmentsListFragment -> {
                            fragment.successRetrieveAppointments(list)
                        }
                    }

                }
            }
    }

    fun getUserVaccinationDetails(userID: String, adapter: Any? = null, holder: Any? = null) {
        firestoreAdapter.collection(Constants.USERS)
            .document(userID)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)!!

                if (adapter != null && adapter is EventAppointmentsAdapter) {
                    adapter.successGetUserInfo(
                        user,
                        holder as EventAppointmentsAdapter.AppointmentsViewHolder
                    )
                } else if (holder != null && holder is EventAppointmentsAdapter.AppointmentsViewHolder)
                    holder.setUserDetails(user)
            }

    }

    fun denyAppointment(id: String) {
        // Method that sets the status of the appointment to "canceled"
        firestoreAdapter.collection(Constants.APPOINTMENTS)
            .document(id)
            .update("status", "cancelled")
    }

    fun acceptAppointment(id: String) {
        firestoreAdapter.collection(Constants.APPOINTMENTS)
            .document(id)
            .update("status", "accepted")
    }

    fun checkForExistingAppointment(eventID: String, activity: Activity) {
        // Check if the current user has an existing appointment for the given event

        firestoreAdapter.collection(Constants.APPOINTMENTS)
            .whereEqualTo("e_id", eventID)
            .whereEqualTo("u_id", getCurrentUserID())
            .get()
            .addOnSuccessListener {
                when (activity) {
                    is EventDetailsActivity -> {
                        if (it.documents.size > 0) activity.successCheckForAppointment(true)
                        else
                            activity.successCheckForAppointment(false)
                    }
                }
            }
            .addOnFailureListener {
                when (activity) {
                    is EventDetailsActivity -> activity.successCheckForAppointment(false)
                }
            }
    }

    fun deleteAppointment(eventID: String) {
        // Delete the appointment made by the user for the given event
        firestoreAdapter.collection(Constants.APPOINTMENTS)
            .whereEqualTo("e_id", eventID)
            .whereEqualTo("u_id", getCurrentUserID())
            .get()
            .addOnSuccessListener {
                for (document in it.documents)
                    document.reference.delete()
            }

    }

//    Events

    fun addEvent(
        activity: EventsActivity,
        name: String,
        address: String,
        phone: String,
        type: String,
        estimatedSurface: Int,
        seatsNumber: Int,
        openSpace: Boolean,
        risk: Int,
        duration: String,
        date: String,
        time: String,
    ) {
        // Test function for adding an event

        val event = Event(
            getCurrentUserID(),
            name,
            address,
            phone,
            type,
            estimatedSurface,
            seatsNumber,
            openSpace,
            risk,
            duration,
            date, time
        )

        // Collection = "events"
        firestoreAdapter.collection(Constants.EVENTS)
            .add(event)
            .addOnSuccessListener { documentReference ->
                // Add the id
                documentReference.update("id", documentReference.id)

                // Add the event id to the user
                firestoreAdapter.collection(Constants.USERS)
                    .document(getCurrentUserID())
                    .collection(Constants.EVENTS)
                    .document(documentReference.id)
                    .set(mapOf(documentReference.id to event.name), SetOptions.merge())
                    .addOnSuccessListener {
                        activity.addEventSuccess()
                    }
                    .addOnFailureListener { e ->
                        Log.e("Add Event", e.toString())
                    }
            }
    }

    fun getEventDetails(eventID: String, activity: Activity) {
        firestoreAdapter.collection(Constants.EVENTS)
            .document(eventID)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("Get Event Details error", e.message.toString())
                }
                if (snapshot != null) {
                    val event = snapshot.toObject(Event::class.java)!!
                    when (activity) {
                        is EventDetailsActivity -> activity.successObserveEvent(event)
                        is ReportActivity -> activity.successRetrieveEvent(event)
                    }
                }

            }
    }

    fun retrieveEvents(activity: Activity) {
        // Method that retrieves the events list
        when (activity) {

            // When called from Events Activity, retrieve only the list for the current user
            is EventsActivity -> {
                firestoreAdapter.collection(Constants.EVENTS)
                    .whereEqualTo(Constants.ADMIN_ID, getCurrentUserID())
                    .addSnapshotListener { document, e ->
                        if (e != null) {
                            Log.e("Retrieve Events", e.message.toString())
                        }

                        if (document != null) {

                            Log.d(activity.javaClass.simpleName, document.documents.toString())
                            val list: ArrayList<Event> = ArrayList()

                            for (d in document.documents) {

                                val event = d.toObject(Event::class.java)!!
                                list.add(event)
                            }

                            activity.successRetrieveEvents(list)
                        }
                    }
            }

            // When called from Places Activity, retrieve all the available places
            is PlacesActivity -> {
                firestoreAdapter.collection(Constants.EVENTS)
                    .get()
                    .addOnSuccessListener { collection ->
                        val list: ArrayList<Event> = ArrayList()

                        for (d in collection.documents) {
                            val event = d.toObject(Event::class.java)!!
                            list.add(event)
                        }

                        activity.successRetrievePlaces(list)
                    }
                    .addOnFailureListener {
                        Log.e(
                            activity.javaClass.simpleName,
                            "Error while retrieving the full events list"
                        )
                    }
            }
        }
    }

    fun getUserName(activity: Activity) {
        firestoreAdapter.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener {
                val userName = it["fullName"] as String
                when (activity) {
                    is EventDetailsActivity -> activity.successRetrieveUserName(userName)
                }
            }
    }

    fun getUpcomingEventsStatistics(activity: AppCompatActivity) {
        firestoreAdapter.collection(Constants.APPOINTMENTS)
            .whereEqualTo("u_id", getCurrentUserID())
            .whereEqualTo("completed", false)
            .whereEqualTo("status", "accepted")
            .addSnapshotListener { document, e ->
                if (e != null) {
                    Log.e("Retrieve Previous Events Statistics", e.message.toString())
                }

                if (document != null) {

                    Log.d(activity.javaClass.simpleName, document.documents.toString())
                    val list: ArrayList<Int> = ArrayList()

                    for (d in document.documents) {

                        val eventAppointment = d.toObject(Appointment::class.java)!!
                        list.add(eventAppointment.risk)
                    }

                    when (activity) {
                        is ReportActivity -> {
                            activity.successGetUpcomingEvents(list)
                        }
                    }
                }
            }

    }

    fun getPreviousEventsStatistics(activity: AppCompatActivity) {
        // retrieve the risk values from the appointments that have been completed and the status is "accepted"

        firestoreAdapter.collection(Constants.APPOINTMENTS)
            .whereEqualTo("u_id", getCurrentUserID())
            .whereEqualTo("completed", true)
            .whereEqualTo("status", "accepted")
            .addSnapshotListener { document, e ->
                if (e != null) {
                    Log.e("Retrieve Previous Events Statistics", e.message.toString())
                }

                if (document != null) {

                    Log.d(activity.javaClass.simpleName, document.documents.toString())
                    val list: ArrayList<Int> = ArrayList()

                    for (d in document.documents) {

                        val eventAppointment = d.toObject(Appointment::class.java)!!
                        list.add(eventAppointment.risk)
                    }

                    when (activity) {
                        is ReportActivity -> {
                            activity.successGetPreviousEvents(list)
                        }
                    }
                }
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileUri: Uri?) {
        val storageReference: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.IMAGE + System.currentTimeMillis() + "." + Constants.getFileExtension(
                activity,
                imageFileUri
            )
        )

        storageReference.putFile(imageFileUri!!).addOnSuccessListener { taskSnapshot ->
            // Successful image upload!
            Log.e(
                "Firebase Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

            // Get the downloadable URL from the task snapshot
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                Log.e("Downloadable Image URL", uri.toString())
                when (activity) {
                    is PhotoActivity -> {
                        activity.imageUploadSuccess(uri.toString())
                    }
                }

            }
        }.addOnFailureListener {
            Log.e("Upload failed", it.message.toString())
        }
    }

    fun uploadImageToUserCollection(imageURL: String, activity: Activity, doseNumber: Int) {
        firestoreAdapter.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(
                "image", imageURL,
                "doseNumber", doseNumber
            )
            .addOnSuccessListener {
                when (activity) {
                    is PhotoActivity -> {
                        activity.imageUpdateUserSuccess()
                    }
                }
            }

    }

    fun retrieveUserImage(userId: String? = null, activity: Activity) {
        // get the image for the current user
        if (userId == null) {
            firestoreAdapter.collection(Constants.USERS)
                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener {
                    var imageUri: String = it["image"] as String
                    when (activity) {
                        is PhotoActivity -> {
                            if (imageUri != "")
                                activity.findImageSuccess(imageUri)
                            else
                                activity.findImageFailure()
                        }
                    }
                }
                .addOnFailureListener {
                    when (activity) {
                        is PhotoActivity ->
                            activity.findImageFailure()
                    }
                }
        } else {
            firestoreAdapter.collection(Constants.USERS)
                .document(userId)
                .get()
                .addOnSuccessListener {
                    val imageUri: String = it["image"] as String
                    when (activity) {
                        is PhotoAppointmentActivity -> {
                            activity.setUserImage(imageUri)
                        }

                    }
                }

        }
    }

    fun retrieveRiskValues(activity: Activity?) {

        firestoreAdapter.collection(Constants.RISKS)
            .addSnapshotListener { document, error ->
                if (error != null)
                    Log.e(TAG, error.message.toString())

                val list: ArrayList<Risk> = ArrayList()

                if (document != null) {
                    for (d in document.documents) {

                        val risk = d.toObject(Risk::class.java)!!
                        list.add(risk)
                    }
                }

                when (activity) {
                    is EventsActivity -> {
                        activity.openAlertDialog(list)
                    }
                }

            }
    }

    fun increaseRisk(type: String, percentage: Double) {
        val reference = firestoreAdapter.collection(Constants.RISKS)
            .whereEqualTo("type", type)
            .get()
            .addOnSuccessListener {
                val document = it.documents[0]

                val risk = document.toObject(Risk::class.java)

                val newRisk = risk!!.value * percentage

                document.reference.update("value", newRisk)
            }
    }

}
