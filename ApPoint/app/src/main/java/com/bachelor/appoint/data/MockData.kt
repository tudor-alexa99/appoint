package com.bachelor.appoint.data

import com.bachelor.appoint.model.Appointment
import java.time.LocalTime

val appointmentsList: MutableList<Appointment> = mutableListOf(
    Appointment(
        "App 1",
        "Danny's Barbershop",
        LocalTime.of(10,30,0),
        false
    ),

    Appointment(
        "app 2",
        "MetroSystems",
        LocalTime.of(20,0,0),
        false
    ),

//    Appointment(
//        "PMT",
//        LocalTime.of(10,30,0),
//        false
//    ),
//
//    Appointment(
//        "Wolfpack Digital",
//        LocalTime.of(10,30,0),
//        false
//    ),
//
//    Appointment(
//        "Burger Store",
//        LocalTime.of(10,30,0),
//        false
//    ),
//
//    Appointment(
//        "Analyst",
//        LocalTime.of(10,30,0),
//        true
//    ),
//
//    Appointment(
//        "Solaris",
//        LocalTime.of(10,30,0),
//        true
//    ),
)

