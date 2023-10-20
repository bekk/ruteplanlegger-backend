package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.*
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.io.IOException
import kotlin.IllegalStateException


@Service
class VegbildeService(val webClient: WebClient.Builder) {

    private val logger = LoggerFactory.getLogger(VegbildeService::class.java)
    public fun getVegbilde(bbox: String): List<VegBilde> {

        val vegbildeURL =
            "https://ogckart-sn1.atlas.vegvesen.no/vegbilder_1_0/ows?service=WFS&version=2.0.0&request=GetFeature&typenames=vegbilder_1_0:Vegbilder_2023&startindex=0&count=10&srsname=urn:ogc:def:crs:EPSG::4326&bbox=${bbox},urn:ogc:def:crs:EPSG:4326&outputformat=application/json"

        try {
            val response = webClient.baseUrl(vegbildeURL).build().get().retrieve().bodyToMono(JsonNode::class.java).block()

            if (response is JsonNode) {

                return vegbildeImages(response)

            }
        }
        catch (ex: IllegalStateException) {
            logger.error("IllegalStateException in vegbildeImages: ${ex.message}")
            throw IllegalStateException(ex.message)
        }
        catch (ex: Exception) {
            logger.error("Exception in getVegbilde: ${ex.message}")
            throw Exception("Couldn´t get response from SVV API")

        }
        return emptyList()
    }

    private fun vegbildeImages(response: JsonNode): MutableList<VegBilde> {

        val listofVegBilder = mutableListOf<VegBilde>()

        try {
            response.get("features").map { bilde ->

                val url = bilde["properties"].get("URL")
                val vegBilde = VegBilde(URL = url.textValue())
                listofVegBilder.add(vegBilde)
            }

            if (listofVegBilder.isEmpty()) {
                throw IllegalStateException("List is empty")
            }

            return listofVegBilder
        }
        catch (ex: IllegalStateException) {
            throw IllegalStateException(ex.message)
        }
        catch (ex: Exception) {
            throw IllegalStateException("Couldn't extract vegbilder from API")
        }

    }
}