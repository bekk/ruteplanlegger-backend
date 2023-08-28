package com.example.ruteplanlegger.Service

import com.example.ruteplanlegger.Model.Rasteplass
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient


fun retrieveNavnAndId(data: JsonNode): List<Rasteplass> {
    return data["objekter"].map { rasteplass ->
        val id = rasteplass["id"].asInt()
        val navn = rasteplass["egenskaper"]
            .find { it["id"].asInt() == 1074 }
            ?.get("verdi")?.asText() ?: ""
        Rasteplass(id, navn)
    }
}

@Service
class RasteplasserService(val webClient: WebClient.Builder) {
    fun getAllRasteplasser(): List<Rasteplass>? {
        val apiURL = "https://nvdbapiles-v3.atlas.vegvesen.no/vegobjekter/39?antall=50&inkluder=egenskaper&inkluder_egenskaper=basis"
        val response = webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()
        if (response is JsonNode) {
            return retrieveNavnAndId(response)
        } else {
            return emptyList()
        }
    }

    // TODO: legge til at id blir sendt dynamisk
    fun getRasteplassById(): String? {
        val apiURL = "https://nvdbapiles-v3.atlas.vegvesen.no/vegobjekter/39/91203457/6"
        return webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(String::class.java).block()
    }
}