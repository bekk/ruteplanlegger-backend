package com.example.ruteplanlegger.model

data class Kjorerute(
    val id: String = "",
    val routeName: String = "",
    val totalDriveTime: Double = 0.0, // total drive time of the route, given in minutes (with delays if supported)
    val totalTime: Double = 0.0, // total time of the route. will mostly be equal to totalDriveTime,
    // but if delays or certain waiting time restrictions are supported, this will be summarized in totalDriveTime
    val totalLength: Double = 0.0,
    val geojson: GeoJSONFeatureCollection,
    val ferryCount: Int = 0,
)