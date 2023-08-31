package com.example.ruteplanlegger.Service

import com.example.ruteplanlegger.Model.Kunst
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

fun createMinimalKunstObject(data: JsonNode): List<Kunst> {
    println(data["objekter"])

    return data["objekter"].map { kunst ->
        val id = kunst["id"].asInt()
        val tittel = kunst["egenskaper"].find {
            it["id"].asInt() == 1733
        }?.get("verdi")?.asText() ?: ""
        val type = kunst["egenskaper"].find {
            it["id"].asInt() == 1101
        }?.get("verdi")?.asText() ?: ""
        val vegnummer = kunst["vegsegmenter"].firstOrNull()
            ?.get("vegsystemreferanse")
            ?.get("vegsystem")
            ?.get("nummer")
        val vegkategori = kunst["vegsegmenter"].firstOrNull()
            ?.get("vegsystemreferanse")
            ?.get("vegsystem")
            ?.get("vegkategori")?.asText()
        val veg = vegkategori + vegnummer
        Kunst(id, tittel, type, veg)
    }

}
    @Service
    class KunstService(val webClient: WebClient.Builder) {
        fun getKunstOgUtsmykking(): List<Kunst>? {
            val apiURL =
                "https://nvdbapiles-v3.atlas.vegvesen.no/vegobjekter/19?antall=50&inkluder=egenskaper,vegsegmenter&inkluder_egenskaper=basis&egenskap=(1101!=null)"
            val response = webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()
            return if (response is JsonNode) createMinimalKunstObject(response) else emptyList()
        }
    }