package com.example.ruteplanlegger.Service

import com.example.ruteplanlegger.Model.Kunst
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

fun retrieveTittelTypeandId(data: JsonNode): List<Kunst> {

    return data["objekter"].map { kunst ->
        val id = kunst["id"].asInt()
        val tittel = kunst["egenskaper"].find {
            it["id"].asInt() == 1733
        }?.get("verdi")?.asText() ?: ""
        val type = kunst["egenskaper"].find {
            it["id"].asInt() == 1101
        }?.get("verdi")?.asText() ?: ""
        Kunst(id, tittel, type)
    }

}

@Service
class KunstService(val webClient: WebClient.Builder) {
    fun getKunstOgUtsmykking(): List<Kunst>? {
        val apiURL =
            "https://nvdbapiles-v3.atlas.vegvesen.no/vegobjekter/19?inkluder=egenskaper&antall=30&egenskap=(1101!=null)"

        val response = webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()
        return if (response is JsonNode) retrieveTittelTypeandId(response) else emptyList()
    }
}