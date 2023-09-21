package com.example.ruteplanlegger.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Geometri(
    val type: String = "Point",
    val coordinates: LatLong
)

data class LatLong(
    val long: Double,
    var lat: Double
)

data class GeoJSONGeometryCollection(
    @JsonProperty("type") val type: String,
    @JsonProperty("geometries") val geometries: List<GeoJSONGeometry>
)

data class GeoJSONGeometry(
    @JsonProperty("type") val type: String,
    @JsonProperty("coordinates") val coordinates: List<List<Double>>
)

