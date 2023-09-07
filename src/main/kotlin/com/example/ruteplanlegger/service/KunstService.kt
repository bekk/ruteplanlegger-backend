package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.Kunst
import com.example.ruteplanlegger.service.utils.createGeometri
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

fun createMinimalKunstObject(data: JsonNode): List<Kunst> {
    return data["objekter"].map { kunst ->

        val id = kunst["id"].asInt()

        val tittel = kunst["egenskaper"].find {
            it["id"].asInt() == 1733
        }?.get("verdi")?.asText() ?: ""

        val kunstType = kunst["egenskaper"].find {
            it["id"].asInt() == 1101
        }?.get("verdi")?.asText() ?: ""

        val vegnummer = kunst["vegsegmenter"].firstOrNull()
            ?.get("vegsystemreferanse")
            ?.get("vegsystem")
            ?.get("nummer")?.asInt() ?: 0

        val vegkategori = kunst["vegsegmenter"].firstOrNull()
            ?.get("vegsystemreferanse")
            ?.get("vegsystem")
            ?.get("vegkategori")?.asText() ?: ""

        val geometry = createGeometri(kunst["geometri"])

        Kunst(
            id = id,
            tittel = tittel,
            kunstType = kunstType,
            vegkategori = vegkategori,
            vegnummer = vegnummer,
            geometri = geometry
        )
    }

}

@Service
class KunstService(val webClient: WebClient.Builder) {
    fun getKunstOgUtsmykking(): List<Kunst>? {
        val apiURL =
            "https://nvdbapiles-v3.atlas.vegvesen.no/vegobjekter/19?antall=50&inkluder=egenskaper,vegsegmenter,geometri&inkluder_egenskaper=basis&egenskap=(1101!=null)&srid=4326"
        val response = webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()
        return if (response is JsonNode) createMinimalKunstObject(response) else emptyList()
    }
}