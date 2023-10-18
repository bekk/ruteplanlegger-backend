package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.*
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.io.IOException


@Service
class VegbildeService(val webClient: WebClient.Builder) {

    public fun getVegbilde(bbox: String): List<Any> {

        val objectMapper = ObjectMapper()
        val vegbildeURL =
            "https://ogckart-sn1.atlas.vegvesen.no/vegbilder_1_0/ows?service=WFS&version=2.0.0&request=GetFeature&typenames=vegbilder_1_0:Vegbilder_2023&startindex=0&count=10&srsname=urn:ogc:def:crs:EPSG::4326&bbox=${bbox},urn:ogc:def:crs:EPSG:4326&outputformat=application/json"
        val listofVegBilder = mutableListOf<Any>()

        val response = webClient.baseUrl(vegbildeURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()

        if (response is JsonNode) {

           response.get("features").map { bilde ->

                            println(bilde["properties"])
                            //val geometri = bilde["geometri"].get("coordinates")
                            val url = bilde["properties"].get("URL")
                            val vegBilde = VegBilde( URL = url.textValue())
                            println(vegBilde)
                            listofVegBilder.add(vegBilde)
            }

            return listofVegBilder


        }
        throw Exception("Couldnt get vegbilder from API")
    }
}