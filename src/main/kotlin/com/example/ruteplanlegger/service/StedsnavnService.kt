package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.LatLong
import com.example.ruteplanlegger.model.Stedsnavn
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class ApiService(val webClient: WebClient.Builder) {
    fun getStedsnavnDataFromAPI(query: String): JsonNode? {
        val stedsnavnApiURL =
            "https://ws.geonorge.no/stedsnavn/v1/navn?sok=$query&fuzzy=true&&maxAnt=10&antPerSide=10&eksakteForst=true"
        val stedsnavnResponse =
            webClient.baseUrl(stedsnavnApiURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()
        return stedsnavnResponse
    }

    fun getAdresseDataFromAPI(query: String): JsonNode? {
        val adresseApiURL =
            "https://ws.geonorge.no/adresser/v1/sok?sok=$query&fuzzy=true&treffPerSide=10&asciiKompatibel=true"
        val adresseResponse =
            webClient.baseUrl(adresseApiURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()
        return adresseResponse
    }
}

@Component
class StedsnavnParser {
    fun parseStedsnavnFromStedsnavnAPI(data: JsonNode) = data["navn"].map { sted ->
        val navn = sted["skrivemåte"].asText()
        val type = sted["navneobjekttype"].asText()
        val fylke = sted["fylker"].get(0)["fylkesnavn"].asText()
        val kommune = sted["kommuner"].get(0)["kommunenavn"].asText()
        val lat = sted["representasjonspunkt"]["nord"].asDouble()
        val long = sted["representasjonspunkt"]["øst"].asDouble()
        Stedsnavn(navn = navn, typeObjekt = type, fylke = fylke, kommune = kommune, koordinat = LatLong(lat, long))
    }

    fun parseStedsnavnFromAdresserAPI(data: JsonNode) = data["adresser"].map { adresse ->
        val navn = adresse["adressetekst"].asText()
        val type = adresse["objtype"].asText()
        var kommune = adresse["kommunenavn"].asText()
        kommune = kommune[0].uppercaseChar() + kommune.substring(1).lowercase();
        val lat = adresse["representasjonspunkt"]["lat"].asDouble()
        val long = adresse["representasjonspunkt"]["lon"].asDouble()
        Stedsnavn(navn = navn, typeObjekt = type, kommune = kommune, koordinat = LatLong(lat, long))
    }
}

@Service
class StedsnavnService(val apiService: ApiService, val stedsnavnParser: StedsnavnParser) {
    fun getStedsnavn(query: String): List<Stedsnavn>? {
        val stedsnavnResponse = apiService.getStedsnavnDataFromAPI(query)
        val adresseResponse = apiService.getAdresseDataFromAPI(query)

        val stedsnavnList =
            if (stedsnavnResponse is JsonNode) stedsnavnParser.parseStedsnavnFromStedsnavnAPI(stedsnavnResponse) else emptyList()
        val adresseList =
            if (adresseResponse is JsonNode) stedsnavnParser.parseStedsnavnFromAdresserAPI(adresseResponse) else emptyList()

        return stedsnavnList + adresseList
    }
}