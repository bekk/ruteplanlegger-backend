package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.LatLong
import com.example.ruteplanlegger.model.Stedsnavn
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient


fun createStedsnavnObject(data: JsonNode) : List<Stedsnavn> {
    return data["navn"].mapIndexed {index, sted ->
        val navn = sted["skrivemåte"].asText()
        val type = sted["navneobjekttype"].asText()
        val lat = sted["representasjonspunkt"]["nord"].asDouble()
        val long = sted["representasjonspunkt"]["øst"].asDouble()
        Stedsnavn(navn = navn, typeObjekt = type, koordinat = LatLong(lat, long), id = index) }


}
@Service
class StedsnavnService (val webClient: WebClient.Builder){
    fun getStedsnavn(query: String): List<Stedsnavn>?{
        val apiURL = "https://ws.geonorge.no/stedsnavn/v1/navn?sok=$query*&maxAnt=10&antPerSide=10&eksakteForst=true"
        val response = webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()
        return if (response is JsonNode) createStedsnavnObject(response) else emptyList()
    }
}