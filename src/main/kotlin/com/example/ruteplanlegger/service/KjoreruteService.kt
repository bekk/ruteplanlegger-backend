package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.GeoJSONFeatureCollection
import com.example.ruteplanlegger.model.Kjorerute
import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.io.IOException


fun createMinimalKjorerute(data: JsonNode): Kjorerute {
    val listOfKjoreruter = data["routes"].map { kjorerute ->
        val id = kjorerute["routeId"].asText()
        val routeName = kjorerute["routeName"].asText()

        val totalDriveTime = kjorerute["statistic"].get("totalDriveTime").asDouble()
        val totalTime = kjorerute["statistic"].get("totalTime").asDouble()
        val totalLength = kjorerute["statistic"].get("totalLength").asDouble()

        val features = kjorerute["features"]
        val geoJSON = GeoJSONFeatureCollection("FeatureCollection", features)
        val ferrycount = kjorerute["statistic"].get("ferryCount").asInt()
        Kjorerute(
            id = id,
            routeName = routeName,
            totalDriveTime = totalDriveTime,
            totalTime = totalTime,
            totalLength = totalLength,
            geojson = geoJSON,
            ferryCount = ferrycount
        )
    }
    return listOfKjoreruter[0] // Return first object only to be sent to frontend
}

@Service
class KjoreruteService(val webClient: WebClient.Builder) {

    private val logger = LoggerFactory.getLogger(KjoreruteService::class.java)
    fun getBestKjoreruter(start: List<Double>, slutt: List<Double>): Kjorerute? {
        // %2C er det samme som ,
        // %3B er det samme som ;
        // Test: http://localhost:8080/kjoreruter?start=59.91187,10.73353&slutt=63.43048,10.39506 // oslo - trondheim
        val (startLat, startLong, sluttLat, sluttLong) = getStartAndStopCoords(start, slutt)

        val apiURL =
            "https://www.vegvesen.no/ws/no/vegvesen/ruteplan/routingservice_v3_0/open/routingService/api/Route/best?Stops=$startLong,$startLat;$sluttLong,$sluttLat&InputSRS=EPSG_4326&OutputSRS=EPSG_4326&ReturnFields=Geometry"
        try {
            val response =
                webClient.baseUrl(apiURL).codecs { it.defaultCodecs().maxInMemorySize(5000 * 1024) } // ca. 5 MB
                    .defaultHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate").build().get().retrieve()
                    .bodyToMono(JsonNode::class.java).block()

            return if (response is JsonNode) createMinimalKjorerute(response) else null
        } catch (ex: Exception) {

            logger.error(ex.toString())
            throw IOException("Couldn´t get kjorerute from API")
        }
    }

    fun getTouristKjoreruter(start: List<Double>, slutt: List<Double>): Kjorerute? {


        val (startLat, startLong, sluttLat, sluttLong) = getStartAndStopCoords(start, slutt)

        val apiURL =
            "https://www.vegvesen.no/ws/no/vegvesen/ruteplan/routingservice_v3_0/open/routingService/api/Route/tourist?Stops=$startLong,$startLat;$sluttLong,$sluttLat&InputSRS=EPSG_4326&OutputSRS=EPSG_4326&ReturnFields=Geometry"

        try {
            val response =
                webClient.baseUrl(apiURL).codecs { it.defaultCodecs().maxInMemorySize(5000 * 1024) } // ca. 5 MB
                    .defaultHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate").build().get().retrieve()
                    .bodyToMono(JsonNode::class.java).block()

            return if (response is JsonNode) createMinimalKjorerute(response) else null
        } catch (ex: Exception) {

            logger.error(ex.toString())
            throw IOException("Couldn´t get kjorerute from API")
        }


    }

    private fun getStartAndStopCoords(start: List<Double>, slutt: List<Double>): List<Double> {
        return listOf(start[0], start[1], slutt[0], slutt[1])
    }
}