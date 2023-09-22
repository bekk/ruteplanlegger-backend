package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.Fotrute
import com.example.ruteplanlegger.model.GeoJSONGeometryCollection
import com.fasterxml.jackson.databind.ObjectMapper
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.rows
import org.jetbrains.kotlinx.dataframe.io.read
import org.jetbrains.kotlinx.dataframe.io.readCSV
import org.springframework.stereotype.Service

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.FileReader
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.locationtech.jts.geom.Geometry


@Service
class FotruteService {
    fun getFotruter(): MutableList<Fotrute> {

        val reader = FileReader("3-fotruter.csv")
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

        val listofFotruter = mutableListOf<Fotrute>()

        for (record in csvParser.records) {

            val navn = record["rutenavn"]
            val lengde = record["meter"].toDouble()
            val ruteFølgerString = record["ruteFølger"].toString().replace("[", "").replace("]", "").replace("'", "")
            val ruteFølger = ruteFølgerString.split(", ")
            val merking = record["merking"].toBoolean()
            val skilting = record["skilting"].toBoolean()
            val gradering = record["gradering"]

            val geometryJsonString = record["geometry"].toString()
            val objectMapper = ObjectMapper()
            val geoJSONFeatureCollection =
                objectMapper.readValue(geometryJsonString, GeoJSONGeometryCollection::class.java)

            val fotrute = Fotrute(
                navn = navn,
                geometri = geoJSONFeatureCollection,
                lengde = lengde,
                ruteFølger = ruteFølger,
                merking = merking,
                skilting = skilting,
                gradering = gradering
            )
            listofFotruter.add(fotrute)
        }

        return listofFotruter
    }
}

