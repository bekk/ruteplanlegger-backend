package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.Rasteplass
import com.example.ruteplanlegger.service.utils.createGeometri
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

fun createMinimalResteplassObject(data: JsonNode): List<Rasteplass> {
    val assosierteToalettanleggID = 220135
    val assosierteUtemoblerID = 220131
    val assosierteLekeapparatID = 220129

    return data["objekter"].map { rasteplass ->

        val id = rasteplass["id"].asInt()

        val navn = rasteplass["egenskaper"].find { it["id"].asInt() == 1074 }?.get("verdi")?.asText() ?: ""

        val vegkategori =
            rasteplass["vegsegmenter"].firstOrNull()?.get("vegsystemreferanse")?.get("vegsystem")?.get("vegkategori")
                ?.asText() ?: ""

        val vegnummer =
            rasteplass["vegsegmenter"].firstOrNull()?.get("vegsystemreferanse")?.get("vegsystem")?.get("nummer")
                ?.asInt() ?: 0

        val geometry = createGeometri(rasteplass["geometri"])

        val toalett = rasteplass["relasjoner"]?.get("barn")?.any { it["listeid"]?.asInt() == assosierteToalettanleggID }
        val utemobler = rasteplass["relasjoner"]?.get("barn")?.any { it["listeid"]?.asInt() == assosierteUtemoblerID }
        val lekeapparat = rasteplass["relasjoner"]?.get("barn")?.any { it["listeid"]?.asInt() == assosierteLekeapparatID }

        Rasteplass(
            id = id,
            navn = navn,
            vegkategori = vegkategori,
            vegnummer = vegnummer,
            geometri = geometry,
            toalett = toalett,
            utemobler = utemobler,
            lekeapparat = lekeapparat
        )
    }
}


@Service
class RasteplasserService(val webClient: WebClient.Builder) {
    fun getAllRasteplasser(): List<Rasteplass>? {
        val apiURL =
            "https://nvdbapiles-v3.atlas.vegvesen.no/vegobjekter/39?kartutsnitt=8.0752408,61.4166883,11.027222,62.2085668&inkluder=vegsegmenter,egenskaper,geometri,relasjoner&inkluder_egenskaper=basis&srid=4326"
        val response = webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()
        return if (response is JsonNode) createMinimalResteplassObject(response) else emptyList()
    }
}