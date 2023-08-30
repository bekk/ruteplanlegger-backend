package com.example.ruteplanlegger.Service

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.reactive.function.client.WebClient

@Service
class KunstService(val webClient: WebClient.Builder) {

    fun getKunstOgUtsmykking(): List<JsonNode>? {
        val apiURL = "https://nvdbapiles-v3.atlas.vegvesen.no/vegobjekter/19?&inkluder=egenskaper&antall=10"
        val response = webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()

        return if (response is JsonNode) response.toList() else emptyList()
    }
}