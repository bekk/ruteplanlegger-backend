package com.example.ruteplanlegger.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

data class Geometri(
    val type: String = "Point",
    val coordinates: LatLong
)

data class LatLong(
    var lat: Double,
    val long: Double
)

data class GeoJSONFeatureCollection(
    @JsonProperty("type") val type: String,
    @JsonProperty("features") val features: JsonNode
)

data class GeoJSONGeometryCollection(
    @JsonProperty("type") val type: String,
    @JsonProperty("geometries") val geometries: List<GeoJSONGeometry>
)

data class GeoJSONGeometry(
    @JsonProperty("type") val type: String,
    @JsonProperty("coordinates") val coordinates: List<List<Double>>
)

