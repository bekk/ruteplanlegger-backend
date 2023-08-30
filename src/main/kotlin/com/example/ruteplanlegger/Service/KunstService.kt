package com.example.ruteplanlegger.Service

import com.example.ruteplanlegger.Model.Kunst
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

fun retrieveTittelTypeandId(data: JsonNode): List<Kunst> {
    val listOfProcessedKunst: MutableList<Kunst> = mutableListOf()
    val listOfKunst = data["objekter"]
    for (kunst in listOfKunst) {
        val id = kunst["id"].asInt()
        var tittel = ""
        var type = ""
        val egenskaper = kunst["egenskaper"]
        for (egenskap in egenskaper) {
            if (egenskap["id"].asInt() == 1733) {
                tittel = egenskap["verdi"].asText()
            }
            if (egenskap["id"].asInt() == 1101) {
                type = egenskap["verdi"].asText()
            }
        }
        val kunst = Kunst(id, tittel, type)
        listOfProcessedKunst.add(kunst)
    }
    return listOfProcessedKunst
}

    @Service
    class KunstService(val webClient: WebClient.Builder) {

        fun getKunstOgUtsmykking(): List<Kunst>? {
            val apiURL = "https://nvdbapiles-v3.atlas.vegvesen.no/vegobjekter/19?&inkluder=egenskaper&antall=30"
            val response = webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()
            return if (response is JsonNode) retrieveTittelTypeandId(response) else emptyList()
        }
    }