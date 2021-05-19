package com.bachelor.appoint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bachelor.appoint.adapters.PlacesAdapter
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.ActivityPlacesBinding
import com.bachelor.appoint.model.Event

class PlacesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlacesBinding
    private lateinit var placesAdapter: PlacesAdapter
    private lateinit var placesList: ArrayList<Event>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind the activity to the view
        binding = ActivityPlacesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Get the list
        getPlacesList()


    }

    private fun getPlacesList() {
        FirestoreClass().retrieveEvents(this)
    }

    fun successRetrievePlaces(placesList: java.util.ArrayList<Event>) {
        this.placesList = placesList

        // Set the recycler view as well
        placesAdapter = PlacesAdapter(this, placesList)

        var recyclerView = binding.rvPlaces
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = placesAdapter
    }
}
