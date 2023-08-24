package com.example.ruteplanlegger.Service

import com.example.ruteplanlegger.Model.Rasteplass
import org.json.JSONObject
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient


// Hente ut ønsket felt (navn, id)
fun convertStringToJsonObject(str: String?): JSONObject? {
    if (str is String) {
        return JSONObject(str)
    }
    return null
}

fun retrieveNavnAndId(obj: JSONObject): List<Rasteplass>? {
    val listOfObjects = obj["objekter"]
    if (listOfObjects is List<*>) {
        print("liste")
    }
    print("hei")
    print(listOfObjects)

    //print(listOfObjects)

    //må forloope gjennom alle rasteplass objektene
    // for hver må vi inn på egenskaper og hente navn og id
    // sett navn + id i en liste
    //returner listen
    return null

}

@Service
class RasteplasserService(val webClient: WebClient.Builder) {
    fun getAllRasteplasser(): List<Rasteplass>? {
        val apiURL = "https://nvdbapiles-v3.atlas.vegvesen.no/vegobjekter/39?antall=50&inkluder=egenskaper"
        val response = webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(String::class.java).block()
        val jsonResponse = convertStringToJsonObject(response)
        if (jsonResponse != null) {
            val rasteplass = retrieveNavnAndId(jsonResponse)
            return rasteplass
        }
        return null
    }

    // TODO: legge til at id blir sendt dynamisk
    fun getRasteplassById(): String? {
        val apiURL = "https://nvdbapiles-v3.atlas.vegvesen.no/vegobjekter/39/91203457/6"
        return webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(String::class.java).block()
    }
}