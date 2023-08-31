package com.example.ruteplanlegger.Service

import com.example.ruteplanlegger.Model.Rasteplass
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient


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
            ?.asText()
            ?: ""

        val veg = vegkategori + vegnummer

        Rasteplass(id = id, navn = navn, veg = veg)
    }
}

@Service
class RasteplasserService(val webClient: WebClient.Builder) {
    fun getAllRasteplasser(): List<Rasteplass>? {
        val apiURL =
            "https://nvdbapiles-v3.atlas.vegvesen.no/vegobjekter/39?antall=50&inkluder=vegsegmenter,egenskaper&inkluder_egenskaper=basis"
        val response = webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()
        return if (response is JsonNode) createMinimalResteplassObject(response) else emptyList()
    }
}