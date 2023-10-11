package com.example.ruteplanlegger.model

data class Kjorerute(
    val id: String = "",
    val routeName: String = "",
    val totalDriveTime: Double = 0.0,
    val totalTime: Double = 0.0, // hva er forskjellen på denne og den over?
    val totalLength: Double = 0.0,
    val geojson: GeoJSONFeatureCollection
)