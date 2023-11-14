package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.GeoJSONFeatureCollection
import com.example.ruteplanlegger.model.Kjorerute
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient


fun createMinimalKjorerute(data: JsonNode): Kjorerute {
    val listOfKjoreruter = data["routes"].map { kjorerute ->
        val id = kjorerute["routeId"].asText()
        val routeName = kjorerute["routeName"].asText()

        val totalDriveTime = kjorerute["statistic"].get("totalDriveTime").asDouble()
        val totalTime = kjorerute["statistic"].get("totalTime").asDouble()
        val totalLength = kjorerute["statistic"].get("totalLength").asDouble()

        val features = kjorerute["features"]
        val geoJSON = GeoJSONFeatureCollection("FeatureCollection", features)

        Kjorerute(
            id = id,
            routeName = routeName,
            totalDriveTime = totalDriveTime,
            totalTime = totalTime,
            totalLength = totalLength,
            geojson = geoJSON
        )
    }
    return listOfKjoreruter[0] // Return first object only to be sent to frontend
}

@Service
class KjoreruteService(val webClient: WebClient.Builder) {

    fun getKjoreruter(start: List<Double>, slutt: List<Double>): Kjorerute? {
        // %2C er det samme som ,
        // %3B er det samme som ;
        // Test: http://localhost:8080/kjoreruter?start=59.91187,10.73353&slutt=63.43048,10.39506 // oslo - trondheim
        val startLat = start[0]
        val startLong = start[1]
        val sluttLat = slutt[0]
        val sluttLong = slutt[1]

        val apiURL =
            "https://www.vegvesen.no/ws/no/vegvesen/ruteplan/routingservice_v3_0/open/routingService/api/Route/best?Stops=$startLong,$startLat;$sluttLong,$sluttLat&InputSRS=EPSG_4326&OutputSRS=EPSG_4326&ReturnFields=Geometry"
        val response = webClient.baseUrl(apiURL).codecs { it.defaultCodecs().maxInMemorySize(5000 * 1024) } // ca. 5 MB
            .defaultHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate").build().get().retrieve()
            .bodyToMono(JsonNode::class.java).block()

        return if (response is JsonNode) createMinimalKjorerute(response) else null
    }

    fun getTouristKjoreruter(start: List<Double>, slutt: List<Double>): Kjorerute? {

        val startLat = start[0]
        val startLong = start[1]
        val sluttLat = slutt[0]
        val sluttLong = slutt[1]

        val apiURL =
            "https://www.vegvesen.no/ws/no/vegvesen/ruteplan/routingservice_v3_0/open/routingService/api/Route/tourist?Stops=$startLong,$startLat;$sluttLong,$sluttLat&InputSRS=EPSG_4326&OutputSRS=EPSG_4326&ReturnFields=Geometry"
        val response = webClient.baseUrl(apiURL).codecs { it.defaultCodecs().maxInMemorySize(5000 * 1024) } // ca. 5 MB
            .defaultHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate").build().get().retrieve()
            .bodyToMono(JsonNode::class.java).block()

        return if (response is JsonNode) createMinimalKjorerute(response) else null

    }
}