package com.bachelor.appoint

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.ActivityReportBinding
import com.bachelor.appoint.model.Appointment
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
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

            FirestoreClass().retrieveLast48HUserAppointments(this)
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

    fun successLast48HAppointmenttts(list: ArrayList<Appointment>) {
        // takes all the appointments of the current user, and checks which ones took place
        // in the last 48 hours. If found, their associated type will have an increase in risk value


    }
}