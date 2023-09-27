package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.Fotrute
import com.example.ruteplanlegger.model.GeoJSONGeometryCollection
import com.example.ruteplanlegger.model.LatLong
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.FileReader

@Service
class FotruteService {
    fun getFotruter(): MutableList<Fotrute> {

        val reader = FileReader("3-fotruter.csv")
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

        val listofFotruter = mutableListOf<Fotrute>()

        val recommendedFotruteList = listOf(515)

        for (record in csvParser.records) {
            val id = record["id"].toInt()
            val navn = record["rutenavn"]
            val lengde = record["meter"].toDouble()
            val beskrivelse = record["beskrivelse"]

            val ruteFølgerString = record["ruteFølger"].toString().replace("[", "").replace("]", "").replace("'", "")
            val ruteFølger = ruteFølgerString.split(", ")

            val merking = record["merking"].toBoolean()
            val skilting = record["skilting"].toBoolean()
            val gradering = record["gradering"]

            val geometryJsonString = record["geometry"].toString()
            val objectMapper = ObjectMapper()
            val geoJSONFeatureCollection =
                objectMapper.readValue(geometryJsonString, GeoJSONGeometryCollection::class.java)

            val anbefalt = id in recommendedFotruteList
            val startLatLong = LatLong(record["startLat"].toDouble(), record["startLong"].toDouble())

            val fotrute = Fotrute(
                id = id,
                navn = navn,
                beskrivelse = beskrivelse,
                geometri = geoJSONFeatureCollection,
                lengde = lengde,
                ruteFølger = ruteFølger,
                merking = merking,
                skilting = skilting,
                gradering = gradering,
                anbefalt = anbefalt,
                startLatLong = startLatLong
            )
            listofFotruter.add(fotrute)
        }

        return listofFotruter
    }
}

