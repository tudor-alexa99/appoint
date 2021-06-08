package com.bachelor.appoint.helpers

import android.content.ContentValues.TAG
import android.util.Log
import com.bachelor.appoint.data.FirestoreClass
import com.bachelor.appoint.model.Risk
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    fun getEstimatedRisk() {

    }


    fun computeEstimatedRisk(
        type: String,
        estimatedSurface: Int,
        seatsNumber: Int,
        estimatedTime: String,
        openSpace: Boolean,
        riskValues: java.util.ArrayList<Risk>
    ): Int {

        val riskValue: Int

        // Get the predefined value
//        val value = associatedRisk.filterValues { it.contains(type) }.keys.toList()[0]
        val value = riskValues.find { risk -> risk.type == type }?.value

        // get the average space for each person. Anything less than 2 square meters will increase the risk.
        val userSpace: Double = (estimatedSurface.toDouble() / seatsNumber)

        // openSpaceValue will be: 0.5 if true, 1 if false
        val openSpaceValue: Double = when (openSpace) {
            true -> 0.5
            false -> 1.0
        }

        // Format estimated time
        val hours = formatTime(estimatedTime)

        // Formula will be: 2 * risk * ( 2 / userSpace) * (estimatedTime / 0.5) * openSpace %
        val risk = (2 * value!! * (2.0 / userSpace) * (hours / 0.5) * openSpaceValue)

        Log.d("Estimated Risk", risk.toString())
        riskValue = risk.toInt()

        return riskValue
    }

    fun formatTime(estimatedTime: String): Double {
        val time = LocalTime.parse(estimatedTime, DateTimeFormatter.ofPattern("H:mm"))
        val hours: Double = time.hour + (time.minute.toDouble() / 60)
        return hours
    }

//    fun successRetrieveRisks(list: java.util.ArrayList<Risk>) {
//        this.riskValues = list
//    }
//
//    fun setValues(riskValues: java.util.ArrayList<Risk>) {
//        this.riskValues = riskValues
//    }

}