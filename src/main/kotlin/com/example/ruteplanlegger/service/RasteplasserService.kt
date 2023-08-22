package com.example.ruteplanlegger.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class RasteplasserService(val webClient: WebClient.Builder) {
    fun getAllRasteplasser(): String? {
        val apiURL = "https://nvdbapiles-v3.atlas.vegvesen.no/vegobjekter/39"
        return webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(String::class.java).block()
        // For liste:
        //return webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(TYPESOMBLIRHENTET::class.java).collectList().block()
    }

    fun getRasteplassById(): String? {
        val apiURL = "https://nvdbapiles-v3.atlas.vegvesen.no/vegobjekter/39/91203457/6"
        return webClient.baseUrl(apiURL).build().get().retrieve().bodyToMono(String::class.java).block()
    }
}