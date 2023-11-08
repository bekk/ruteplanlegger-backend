package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.Rasteplass
import com.example.ruteplanlegger.service.utils.createGeometri
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Component
class RasteplassParser {
    fun createRasteplassList(data: JsonNode): List<Rasteplass> {
        val assosierteToalettanleggID = 220135
        val assosierteUtemoblerID = 220131
        val assosierteLekeapparatID = 220129
        val recommendedRasteplassList =
            listOf(91204143, 91027709, 90929924, 657582549, 218660593, 779730581, 91204130, 91204128)

        return data["objekter"].map { rasteplass ->

            val id = rasteplass["id"].asInt()

            val navn = rasteplass["egenskaper"].find { it["id"].asInt() == 1074 }?.get("verdi")?.asText() ?: ""

            val vegkategori =
                rasteplass["vegsegmenter"].firstOrNull()?.get("vegsystemreferanse")?.get("vegsystem")
                    ?.get("vegkategori")
                    ?.asText() ?: ""

            val vegnummer =
                rasteplass["vegsegmenter"].firstOrNull()?.get("vegsystemreferanse")?.get("vegsystem")?.get("nummer")
                    ?.asInt() ?: 0

            val geometry = createGeometri(rasteplass["geometri"])

            val toalett =
                rasteplass["relasjoner"]?.get("barn")?.any { it["listeid"]?.asInt() == assosierteToalettanleggID }
            val utemobler =
                rasteplass["relasjoner"]?.get("barn")?.any { it["listeid"]?.asInt() == assosierteUtemoblerID }
            val lekeapparat =
                rasteplass["relasjoner"]?.get("barn")?.any { it["listeid"]?.asInt() == assosierteLekeapparatID }
            val anbefalt = id in recommendedRasteplassList

            Rasteplass(
                id = id,
                navn = navn,
                vegkategori = vegkategori,
                vegnummer = vegnummer,
                geometri = geometry,
                toalett = toalett,
                utemobler = utemobler,
                lekeapparat = lekeapparat,
                anbefalt = anbefalt
            )
        }
    }
}

@Component
class Getters {
    fun getApiURL(start: String?): String {
        val defaultApiURL =
            "https://nvdbapiles-v3.atlas.vegvesen.no/vegobjekter/39?inkluder=vegsegmenter,egenskaper,geometri,relasjoner&inkluder_egenskaper=basis&srid=4326"
        if (start != null) {
            return "$defaultApiURL&start=$start"
        }
        return defaultApiURL
    }

    fun getTokenToNextPage(metadata: JsonNode): String {
        return metadata["neste"].get("start").asText()
    }

    fun getAntallReturnert(data: JsonNode): Int {
        val metadata = data["metadata"]
        return metadata["returnert"].asInt()
    }

    fun getAntall(data: JsonNode): Int {
        val metadata = data["metadata"]
        return metadata["antall"].asInt()
    }
}

@Service
class ApiRasteplassService(val webClient: WebClient.Builder, val getters: Getters) {
    fun getRasteplasserFromSVV(start: String?): JsonNode? {
        val apiURL = getters.getApiURL(start)
        val response =
            webClient.baseUrl(apiURL).codecs { it.defaultCodecs().maxInMemorySize(3000 * 1024) }.build().get()
                .retrieve().bodyToMono(JsonNode::class.java).block()
        return if (response is JsonNode) response else null
    }

}

@Component
class RasteplassFetcher {
    fun fetchAllRasteplasser(
        getters: Getters,
        rasteplassParser: RasteplassParser,
        apiService: ApiRasteplassService
    ): List<Rasteplass> {

        val allRasteplasser: MutableList<Rasteplass> = mutableListOf()

        var totaltReturnert = 0

        var response = apiService.getRasteplasserFromSVV(start = null)

        if (response != null) {
            showMetadata(response["metadata"])

            val totaltAntallRasteplasser = getters.getAntall(response)
            val antallReturnert = getters.getAntallReturnert(response)

            totaltReturnert += antallReturnert

            val listOfFirstRasteplasser = rasteplassParser.createRasteplassList(response)

            allRasteplasser.addAll(listOfFirstRasteplasser)

            while (totaltReturnert < totaltAntallRasteplasser) {
                val nextToken = getters.getTokenToNextPage(response!!["metadata"])
                response = apiService.getRasteplasserFromSVV(nextToken)
                if (response != null) {
                    showMetadata(response["metadata"])
                    val nextReturnert = getters.getAntallReturnert(response)
                    val listOfNextRasteplasser = rasteplassParser.createRasteplassList(response)
                    totaltReturnert += nextReturnert

                    allRasteplasser.addAll(listOfNextRasteplasser)
                }
            }
        }

        println(allRasteplasser.size)

        return allRasteplasser
    }

    private fun showMetadata(metadata: JsonNode) {
        val antall = metadata["antall"].asInt()
        val returnert = metadata["returnert"].asInt()
        val sidestorrelse = metadata["sidestørrelse"].asInt()
        val neste = metadata["neste"].get("start").asText()
        val info = "Antall: $antall, Returnert: $returnert, Sidestørrelse: $sidestorrelse, Token til neste: $neste"

        println(info)
    }

    fun fetchRecommendedRasteplasser(data: List<Rasteplass>): List<Rasteplass> {
        return data.filter { it.anbefalt }
    }
}

@Service
class RasteplassService(
    val rasteplassParser: RasteplassParser,
    val getters: Getters,
    val apiService: ApiRasteplassService,
    val rasteplassFetcher: RasteplassFetcher
) {
    fun getRasteplasser(): List<Rasteplass>? {
        return rasteplassFetcher.fetchAllRasteplasser(getters, rasteplassParser, apiService)
    }

    fun getRecommendedRasteplasser(): List<Rasteplass>? {
        val allRasteplasser = rasteplassFetcher.fetchAllRasteplasser(getters, rasteplassParser, apiService)
        return rasteplassFetcher.fetchRecommendedRasteplasser(allRasteplasser)
    }
}

