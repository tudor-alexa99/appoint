package com.bachelor.appoint.helpers

import android.util.Log
import java.text.Format
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// Class that will be used to compute statistics: Event Risk value and contamination chance
class StatisticsHelper {
    /* The risk values associated will be denoted from 10 to 90 %
    * A weighted sum will be computed,based on a certain Event's variables such as:
    * - The area of the place
    * - Being open space or enclosed
    * - The estimated time people spend on average
    * - The number of people that can be seated, divided by the total surface
    */

    private val associatedRisk = mapOf<Int, Array<String>>(
        1 to arrayOf(
            "Dining",
            "Hiking",
        ),
        2 to arrayOf(
            "Picnic",
            "Pool party",
            "Beach party",
        ),
        3 to arrayOf(
            "Kids playdate",
            "Salon visit",
            "Dentist office visit",
        ),
        4 to arrayOf(
            "Outside drinking",
            "Gym classes",
            "Indoors party",
            "Blind date",
            "Office meeting",
            "Science conference",
        ),
        5 to arrayOf(
            "Sports Competition",
            "Pub",
            "Clubbing",
            "Festival",
        )
    )


    fun computeEstimatedRisk(
        type: String,
        estimatedSurface: Int,
        seatsNumber: Int,
        estimatedTime: String,
        openSpace: Boolean
    ): Int {
        // Returns the estimated risk associated with a event

        // Get the predefined value
        val key = associatedRisk.filterValues { it.contains(type) }.keys.toList()[0]

        // get the average space for each person. Anything less than 2 square meters will increase the risk.
        val userSpace: Double = (estimatedSurface.toDouble()/seatsNumber)

        // openSpaceValue will be: 0.5 if true, 1 if false
        val openSpaceValue: Double = when (openSpace) {
            true -> 0.5
            false -> 1.0
        }

        // Format estimated time
        val hours = formatTime(estimatedTime)

        // Formula will be: 2 * risk * ( 2 / userSpace) * (estimatedTime / 0.5) * openSpace %
        val risk = (2 * key * (2.0 / userSpace) * (hours / 0.5) * openSpaceValue)

        Log.d("Estimated Risk", risk.toString())
        return risk.toInt()


    }

    fun formatTime(estimatedTime: String): Double {
        val time = LocalTime.parse(estimatedTime, DateTimeFormatter.ofPattern("H:mm"))
        val hours: Double = time.hour + (time.minute.toDouble() / 60)
        return hours
    }

}