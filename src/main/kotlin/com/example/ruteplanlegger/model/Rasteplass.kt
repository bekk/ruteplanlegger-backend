package com.example.ruteplanlegger.model

data class Geometri(
    val type: String = "Point",
    val coordinates: LatLong
)

data class LatLong(
    val long: Double,
    var lat: Double
)

data class Rasteplass(
    val id: Int = 0,
    val navn: String = "",
    val vegkategori: String = "",
    val vegnummer: Int = 0,
    val geometri: Geometri
)