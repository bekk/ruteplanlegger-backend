package com.example.ruteplanlegger.model

data class Geometri(
    val type: String = "Point",
    val coordinates: LatLong
)

data class LatLong(
    val long: Double,
    var lat: Double
)