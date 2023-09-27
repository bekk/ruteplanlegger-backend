package com.example.ruteplanlegger.model


data class Fotrute(
    val id: Int = 0,
    val navn: String = "",
    val type: String = "Fotrute",
    val beskrivelse: String,
    val lengde: Double = 0.0,
    val geometri: Any,
    val ruteFølger: List<String>,
    val merking: Boolean = false,
    val skilting: Boolean = false,
    val gradering: String,
    val anbefalt: Boolean = false,
    val startLatLong: LatLong,
    )