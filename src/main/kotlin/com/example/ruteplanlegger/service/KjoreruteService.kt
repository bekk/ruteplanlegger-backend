package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.GeoJSONFeatureCollection
import com.example.ruteplanlegger.model.Kjorerute
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient


fun createMinimalKjorerute(data: JsonNode): List<Kjorerute> {
    return data["routes"].map { kjorerute ->
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
}

@Service
class KjoreruteService(val webClient: WebClient.Builder) {
    fun getKjoreruteOsloHamar(): List<Kjorerute>? {
        // %2C er det samme som ,
        // %3B er det samme som ;
        val apiURL =
            "https://www.vegvesen.no/ws/no/vegvesen/ruteplan/routingservice_v3_0/open/routingService/api/Route/best?Stops=10.74609,59.91273;11.06798,60.7945&InputSRS=EPSG_4326&OutputSRS=EPSG_4326&ReturnFields=Geometry"
        val response = webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()

        return if (response is JsonNode) createMinimalKjorerute(response) else emptyList()
    }
}