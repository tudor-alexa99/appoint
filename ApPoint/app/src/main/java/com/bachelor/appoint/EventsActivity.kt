package com.bachelor.appoint

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bachelor.appoint.adapters.EventsListAdapter
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.ActivityEventsBinding
import com.bachelor.appoint.databinding.FragmentAddEventAlertBinding
import com.bachelor.appoint.helpers.StatisticsHelper
import com.bachelor.appoint.model.Event
import com.bachelor.appoint.ui.AppointmentsListFragment
import com.bachelor.appoint.ui.EventInformationFragment
import com.bachelor.appoint.utils.Constants
import java.util.*
import kotlin.collections.ArrayList


class EventsActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: ActivityEventsBinding
    private lateinit var eventAdapter: EventsListAdapter
    private lateinit var eventList: ArrayList<Event>

    var _time = ""
    var _date = ""
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind the activity to the view
        binding = ActivityEventsBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        // Retrieve the list
        getEventList()

        // Add event button
        binding.btnAddEvent.setOnClickListener {
            print("Button clicked")
            Log.d("Add event button clicked", "Event Activity")
            openAlertDialog()
        }

        itemTouchHelper.attachToRecyclerView(binding.rvEvent)

    }


    override fun onResume() {
        super.onResume()
        getEventList()
    }

    // ADD

    fun openAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add new event")
        val alertBinding = FragmentAddEventAlertBinding.inflate(layoutInflater)
        val alertView = alertBinding.root
        builder.setView(alertView)

        // initialise the spinner
        val spinner = alertBinding.spEventTypes

        // Create the adaptor and set its values
        ArrayAdapter.createFromResource(
            this,
            R.array.event_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        // Set the timePicker button
        alertBinding.bntDateTime.setOnClickListener(View.OnClickListener {
            pickDate()
        })

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            val name: String = alertBinding.etEventName.text.toString()
            val address: String = alertBinding.etEventAddress.text.toString()
            val phoneNumber: String = alertBinding.etPhone.text.toString()
            val type: String = spinner.selectedItem.toString()
            val estimatedSurface: Int = alertBinding.etEstimatedSurface.text.toString().toInt()
            val maxSeatsNumber: Int = alertBinding.etSeatsNumber.text.toString().toInt()
            val duration: String = alertBinding.etTimeSpent.text.toString()
            val openSpace: Boolean = alertBinding.swiOpenSpace.isChecked

            val estimatedRisk = StatisticsHelper().computeEstimatedRisk(
                type,
                estimatedSurface,
                maxSeatsNumber,
                duration,
                openSpace
            )

            FirestoreClass().addEvent(
                this,
                name,
                address,
                phoneNumber,
                type,
                estimatedSurface,
                maxSeatsNumber,
                openSpace,
                estimatedRisk,
                duration,
                _date,
                _time
            )
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, which ->
            Toast.makeText(
                applicationContext,
                android.R.string.cancel, Toast.LENGTH_SHORT
            ).show()
        }

        builder.show()
    }

    private fun pickDate() {
        pickDateTimeCalendar()
        DatePickerDialog(this, this, year, month, day).show()
    }

    private fun pickDateTimeCalendar() {
        val calendar: Calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        TimePickerDialog(this, this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute
        _date = "${savedDay}/${savedMonth + 1}/${savedYear}"
        _time = "${savedHour}:${savedMinute}"

        Toast.makeText(
            applicationContext,
            "Date ${_date} and time ${_time} saved!", Toast.LENGTH_LONG
        ).show()

    }

    fun addEventSuccess() {
        Toast.makeText(
            applicationContext,
            "Event added!", Toast.LENGTH_LONG
        ).show()
    }

    // READ

    private fun getEventList() {
        FirestoreClass().retrieveEvents(this)
    }

    fun successRetrieveEvents(eventList: ArrayList<Event>) {
        this.eventList = eventList

        // Set the recycler view as well
        eventAdapter = EventsListAdapter(this, eventList)

        var recyclerView = binding.rvEvent
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = eventAdapter

    }

    // Used for focusing on the list when scrolling vertically
    private fun scrollToPosition(direction: Int) {
        // direction: -1 -> left, 1 -> right

//        val eventId = "someId"
//        val position: Int = eventAdapter.getItemPosition(eventId)
//        if (position >= 0) {
//            binding.rvEvent.scrollToPosition(position)
//        }

        if (direction == 1) {
            eventAdapter.nextPosition()
            binding.rvEvent.scrollToPosition(eventAdapter.getCurrentPosition())
        }
        if (direction == -1) {
            eventAdapter.previousPosition()
            binding.rvEvent.scrollToPosition(eventAdapter.getCurrentPosition())
        }
    }

    var itemTouchHelper = ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder, target: ViewHolder
            ): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                // move item in `fromPos` to `toPos` in adapter.
                return true // true if moved, false otherwise
            }

            // Swipe up on the event card to reload the appointments associated with it. An appointments list will reload and you will be able so swipe left or right on it `
            // 2 fragments. On swipe up, load the event information, on swipe down load the appointments list

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                Log.d("Direction", direction.toString())
                eventAdapter.notifyItemChanged(viewHolder.adapterPosition);

                // Select the current event from the card that was swiped on
                var event: Event = eventAdapter.getItem(viewHolder.adapterPosition)

                // Create a bundle to send the event param to the fragment
                val bundle = Bundle()
                bundle.putParcelable(Constants.EVENT, event)

                // used for switching between fragment in the same frame
                val manager = supportFragmentManager

                if (direction == 1) {   // on Swipe UP
                    // create the new fragment and send the parameters
                    val infoFragment = EventInformationFragment()
                    infoFragment.arguments = bundle

                    binding.ellipse5.animate().alpha(0.0f)
                    binding.ellipse4.visibility = View.VISIBLE
                    binding.ellipse4.animate().alpha(1.0f)
//                    binding.ellipse3.visibility  = View.GONE


                    // initiate the transaction that replaces the fragments
                    val transaction = manager.beginTransaction().setCustomAnimations(
                        android.R.animator.fade_in,
                        android.R.animator.fade_out
                    )
                    transaction.replace(R.id.fr_event_info, infoFragment)
                    transaction.commit()

                } else if (direction == 2) {    // on Swipe DOWN
                    // create the new fragment and send the parameters
                    val appListFragment = AppointmentsListFragment()
                    appListFragment.arguments = bundle

                    binding.ellipse4.animate().alpha(0.0f)
//                    binding.ellipse4.visibility  = View.GONE
                    binding.ellipse5.animate().alpha(1.0f)
                    binding.ellipse5.visibility = View.VISIBLE


                    // initiate the transaction that replaces the fragments
                    val transaction = manager.beginTransaction().setCustomAnimations(
                        android.R.animator.fade_in,
                        android.R.animator.fade_out
                    )
                    transaction.replace(R.id.fr_event_info, appListFragment)
                    transaction.commit()
                }
            }
        })
}
