package com.bachelor.appoint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.databinding.ActivityReportBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAChart
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AADataLabels
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAMarker
import java.util.ArrayList

class ReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportBinding
    private lateinit var previousEvents: Array<Any>
    private lateinit var upcomingEvents: Array<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // retrieve the events informtation
        this.getStatistics()

        // set the button actions
        this.setPhotoButton()

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
}