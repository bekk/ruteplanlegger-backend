package com.example.ruteplanlegger.model


data class Fotrute(
    val navn: String = "",
    val type: String = "Fotrute",
    val lengde: Double = 0.0,
    val geometri: Any,
    val ruteFølger: String = "",
    val merking: String = "",
    val skilting: Boolean = false

    )