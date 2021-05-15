package com.bachelor.appoint.helpers

import android.util.Log
import java.text.Format
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// Class that will be used to compute statistics: Business Risk value and contamination chance
class StatisticsHelper {
    /* The risk values associated will be denoted from 10 to 90 %
    * A weighted sum will be computed,based on a certain business's variables such as:
    * - The area of the place
    * - Being open space or enclosed
    * - The estimated time people spend on average
    * - The number of people that can be seated, divided by the total surface
    */

    private val associatedRisk = mapOf<Int, Array<String>>(
        1 to arrayOf(
            "Clinic",
            "Dentist"
        ),
        2 to arrayOf(
            "Pool",
            "Hair Salon"
        ),
        3 to arrayOf(
            "Dining",
            "Hotel",
            "Insurance company",
            "Office"
        ),
        4 to arrayOf(
            "Kids playground",
            "Bank",
            "Pub",
        ),
        5 to arrayOf(
            "Gym",
            "Sports Hall",
            "Club",
        )
    )


    fun computeEstimatedRisk(
        type: String,
        estimatedSurface: Int,
        seatsNumber: Int,
        estimatedTime: String,
        openSpace: Boolean
    ) {
        // Returns the estimated risk associated with a business

        // Get the predefined value
        val key = associatedRisk.filterValues { it.contains(type) }.keys.toList()[0]

        // get the average space for each person. Anything less than 2 square meters will increase the risk.
        val userSpace: Double = (estimatedSurface.toDouble()/seatsNumber)

        // openSpaceValue will be: 0.5 if true, 1 if false
        val openSpaceValue: Double
        when (openSpace) {
            true -> openSpaceValue = 0.5
            false -> openSpaceValue = 1.0
        }

        // Format estimated time
        val hours = formatTime(estimatedTime)

        // Formula will be: risk * ( 2 / userSpace) * (estimatedTime / 0.5) * openSpace %
        val risk = (key * (2.0 / userSpace) * (hours / 0.5) * openSpaceValue)

        Log.d("Estimated Risk", risk.toString())


    }

    fun formatTime(estimatedTime: String): Double {
        val time = LocalTime.parse(estimatedTime, DateTimeFormatter.ofPattern("H:mm"))
        val hours: Double = time.hour + (time.minute.toDouble() / 60)
        return hours
    }

}