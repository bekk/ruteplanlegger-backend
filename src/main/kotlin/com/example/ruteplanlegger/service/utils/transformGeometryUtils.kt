package com.example.ruteplanlegger.service.utils

import com.example.ruteplanlegger.model.Geometri
import com.example.ruteplanlegger.model.LatLong
import com.fasterxml.jackson.databind.JsonNode
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.io.WKTReader


fun createGeometri(geometri: JsonNode): Geometri {
    val wktNode = geometri["wkt"]
    val wkt = wktNode.asText()
    val wktReader = WKTReader()
    val geometry: Geometry = wktReader.read(wkt)
    val centroid =
        geometry.centroid //gir et Point som er ca. i midten av en Linestring/Polygon
    val lat: Double = centroid.coordinate.y
    val long: Double = centroid.coordinate.x
    val latLongObject = LatLong(long, lat)

    return Geometri(type = "Point", coordinates = latLongObject)
}