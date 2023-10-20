package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.*
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.io.IOException


@Service
class VegbildeService(val webClient: WebClient.Builder) {

    public fun getVegbilde(bbox: String): List<VegBilde> {

        val vegbildeURL =
            "https://ogckart-sn1.atlas.vegvesen.no/vegbilder_1_0/ows?service=WFS&version=2.0.0&request=GetFeature&typenames=vegbilder_1_0:Vegbilder_2023&startindex=0&count=10&srsname=urn:ogc:def:crs:EPSG::4326&bbox=${bbox},urn:ogc:def:crs:EPSG:4326&outputformat=application/json"

        try {
            val response = webClient.baseUrl(vegbildeURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()

            if (response is JsonNode) {

                return vegbildeImages(response)

            }
            else {
                throw Exception("Couldnt get vegbilder from API")
            }
        }

        catch (ex: Exception) {
            throw Exception("Couldn´t get response from SVV API", ex)

        }




    }

    private fun vegbildeImages(response: JsonNode): MutableList<VegBilde> {

        val listofVegBilder = mutableListOf<VegBilde>()

        response.get("features").map { bilde ->

            val url = bilde["properties"].get("URL")
            val vegBilde = VegBilde(URL = url.textValue())
            listofVegBilder.add(vegBilde)
        }

        return listofVegBilder
    }
}