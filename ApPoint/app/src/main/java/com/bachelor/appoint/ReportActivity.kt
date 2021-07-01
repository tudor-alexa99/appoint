package com.bachelor.appoint

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.ActivityReportBinding
import com.bachelor.appoint.helpers.StatisticsHelper
import com.bachelor.appoint.model.Appointment
import com.bachelor.appoint.model.Event
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.ArrayList

class ReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportBinding
    private lateinit var previousEvents: Array<Any>
    private lateinit var upcomingEvents: Array<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // retrieve the events information
        this.getStatistics()

        // set the photo button actions
        this.setPhotoButton()

        // set the report button actions
        setReportButton()

    }

    private fun setReportButton() {
        // create an alert dialog that will ask for the user to confirm contamination
        // when confirmed, all the appointments made within the next 2 weeks will be cancelled
        // all the types of events that took place in the past 48 hours will have an incremented risk value

        val builder = AlertDialog.Builder(this)
        builder.setTitle("I have contacted the virus!")
        builder.setMessage(
            "Confirming this will cancel all of your appointments that take" +
                    " place in the next 14 days. \n " +
                    "Statistics will be built using this information."
        )

        builder.setPositiveButton("Confirm") { _, _ ->
            Log.d(TAG, "Confirm clicked")
            // First, retrieve all the events the user has attended in the last 48 hours

            FirestoreClass().retrieveUserAppointments(viewModel = null, activity = this)
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            Log.d(TAG, "Cancel clicked")
        }

        binding.btnReportContact.setOnClickListener {
            builder.show()
        }
    }

    private fun setPhotoButton() {
        binding.btnUploadPhoto.setOnClickListener {
            intent = Intent(this@ReportActivity, PhotoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getStatistics() {
        // first, get the stats for the upcoming events. Once we get the callback, retrieve the data for previous events

        FirestoreClass().getPreviousEventsStatistics(this)
    }


    fun setChartContent() {

        var aaChartView = binding.aaChartView

        val aaChartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Areaspline)
            .subtitle("Risk value for upcoming events")
            .backgroundColor("#4D000000")
            .dataLabelsEnabled(false)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Completed event(s) risk")
                        .data(this.previousEvents)
                        .reversed(true),
                    AASeriesElement()
                        .name("Upcoming event(s) risk")
                        .data(this.upcomingEvents)
                        .color("#85C7B5"),
                )
            )
        aaChartView.aa_drawChartWithChartModel(aaChartModel)
    }

    fun successGetPreviousEvents(list: ArrayList<Int>) {
        this.previousEvents = list.toArray()
        // when completed, retrieve the upcoming events as well

        FirestoreClass().getUpcomingEventsStatistics(this)

    }


    fun successGetUpcomingEvents(list: ArrayList<Int>) {
        this.upcomingEvents = list.toArray()
        // when completed, fill the chart content

        setChartContent()

    }

    fun successRetrieveUserAppointments(list: ArrayList<Appointment>) {
        // takes all the appointments of the current user, and checks which ones took place
        // in the last 48 hours. If found, their associated type will have an increase in risk value

        for (app in list) {
            // get the time difference between now and the time the event took place
            val difference = StatisticsHelper().getTimeDifference(app.date, app.startTime)

            StatisticsHelper().initFormatters()

            // if the event took place in the last 48 hours, increase the risk value
            if (difference != null) {
                if (difference.toHours() in -48..0)
                    increaseRiskValue(app.e_id)
                else
                // if the appointment is made for an upcoming event taking place in the next 14 days, cancel the appointment
                    if (difference.toDays() in 0..14)
                        cancelAppointment(app)

            }
        }

    }

    private fun cancelAppointment(appointment: Appointment) {
        FirestoreClass().denyAppointment(appointment.id)
    }

    private fun increaseRiskValue(eventID: String) {
        // first, get the corresponding event
        FirestoreClass().getEventDetails(eventID, this)
    }

    fun successRetrieveEvent(event: Event) {
        // when an event is successfully retrieved, compute the risk percentage and increase the value

        // for example, if 1 out of 50 people gets contaminated, meaning 2% of the total participants
        // the associated risk value will be increased to 102%
        val percentage = ((1.0 / event.seatsNumber) * 100 + 100) / 100

        // set the new value
        FirestoreClass().increaseRisk(event.type, percentage)
    }
}
