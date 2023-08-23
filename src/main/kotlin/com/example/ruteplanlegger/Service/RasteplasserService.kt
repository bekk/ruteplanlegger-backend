package com.example.ruteplanlegger.Service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class RasteplasserService(val webClient: WebClient.Builder) {
    fun getAllRasteplasser(): String? {
        val apiURL = "https://nvdbapiles-v3.atlas.vegvesen.no/vegobjekter/39?antall=50&inkluder=egenskaper"
        return webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(String::class.java).block()
    }

    // TODO: legge til at id blir sendt dynamisk
    fun getRasteplassById(): String? {
        val apiURL = "https://nvdbapiles-v3.atlas.vegvesen.no/vegobjekter/39/91203457/6"
        return webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(String::class.java).block()
    }
}