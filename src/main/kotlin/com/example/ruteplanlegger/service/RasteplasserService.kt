package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.Geometri
import com.example.ruteplanlegger.model.LatLong
import com.example.ruteplanlegger.model.Rasteplass
import com.fasterxml.jackson.databind.JsonNode
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.io.WKTReader
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

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

fun createMinimalResteplassObject(data: JsonNode): List<Rasteplass> {
    return data["objekter"].map { rasteplass ->

        val id = rasteplass["id"].asInt()
        val navn = rasteplass["egenskaper"]
            .find { it["id"].asInt() == 1074 }
            ?.get("verdi")?.asText() ?: ""

        val vegkategori = rasteplass["vegsegmenter"]
            .firstOrNull()
            ?.get("vegsystemreferanse")
            ?.get("vegsystem")
            ?.get("vegkategori")
            ?.asText()
            ?: ""

        val vegnummer = rasteplass["vegsegmenter"]
            .firstOrNull()
            ?.get("vegsystemreferanse")
            ?.get("vegsystem")
            ?.get("nummer")
            ?.asInt()
            ?: 0

        val geometry = createGeometri(rasteplass["geometri"])

        Rasteplass(id = id, navn = navn, vegkategori = vegkategori, vegnummer = vegnummer, geometri = geometry)
    }
}

@Service
class RasteplasserService(val webClient: WebClient.Builder) {
    fun getAllRasteplasser(): List<Rasteplass>? {
        val apiURL =
            "https://nvdbapiles-v3.atlas.vegvesen.no/vegobjekter/39?antall=50&inkluder=vegsegmenter,egenskaper,geometri&inkluder_egenskaper=basis&srid=4326"
        val response = webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()
        return if (response is JsonNode) createMinimalResteplassObject(response) else emptyList()
    }
}