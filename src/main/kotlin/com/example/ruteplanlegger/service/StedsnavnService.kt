package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.LatLong
import com.example.ruteplanlegger.model.Stedsnavn
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

var index = -1;

fun createStedsnavnObject(data: JsonNode) = data["navn"].map { sted ->
    val navn = sted["skrivemåte"].asText()
    val type = sted["navneobjekttype"].asText()
    val lat = sted["representasjonspunkt"]["nord"].asDouble()
    val long = sted["representasjonspunkt"]["øst"].asDouble()
    index++
    Stedsnavn(navn = navn, typeObjekt = type, koordinat = LatLong(lat, long), id = index)

}

fun createStedsnavnObjectFromAdresser(data: JsonNode) = data["adresser"].map { adresse ->
    val navn = adresse["adressetekst"].asText()
    val type = adresse["objtype"].asText()
    val lat = adresse["representasjonspunkt"]["lat"].asDouble()
    val long = adresse["representasjonspunkt"]["lon"].asDouble()
    index++
    Stedsnavn(navn = navn, typeObjekt = type, koordinat = LatLong(lat, long), id = index)
}

@Service
class StedsnavnService(val webClient: WebClient.Builder) {
    fun getStedsnavn(query: String): List<Stedsnavn>? {
        val stedsnavnApiURL =
            "https://ws.geonorge.no/stedsnavn/v1/navn?sok=$query&fuzzy=true&&maxAnt=10&antPerSide=10&eksakteForst=true"
        val stedsnavnResponse =
            webClient.baseUrl(stedsnavnApiURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()

        val adresseApiURL =
            "https://ws.geonorge.no/adresser/v1/sok?sok=$query&fuzzy=true&treffPerSide=10&asciiKompatibel=true"
        val adresseResponse =
            webClient.baseUrl(adresseApiURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()

        val stedsnavnList = if (stedsnavnResponse is JsonNode) createStedsnavnObject(stedsnavnResponse) else emptyList()
        val adresseList =
            if (adresseResponse is JsonNode) createStedsnavnObjectFromAdresser(adresseResponse) else emptyList()

        return stedsnavnList + adresseList
    }
}