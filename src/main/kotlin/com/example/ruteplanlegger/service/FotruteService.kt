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
        val csvFilePath = "3-fotruter.csv"
        val reader = FileReader(csvFilePath)
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

        val listofFotruter = mutableListOf<Fotrute>()

        for (record in csvParser.records) {
            println(record)
            val navn = record["rutenavn"]
            val geometryJsonString = record["geometry"].toString()
            // Create an ObjectMapper instance
            val objectMapper = ObjectMapper()

            // Deserialize the GeoJSON string into a GeoJSONFeatureCollection object
            val geoJSONFeatureCollection =
                objectMapper.readValue(geometryJsonString, GeoJSONGeometryCollection::class.java)
            println(geometryJsonString)
            println(geoJSONFeatureCollection)
            val lengde = record["meter"].toDouble()
            val ruteFølgerString = record["ruteFølger"].toString().replace("[", "").replace("]", "").replace("'", "")
            val ruteFølger = ruteFølgerString.split(", ")
            val merking = record["merking"].toBoolean()
            val skilting = record["skilting"].toBoolean()
            val gradering = record["gradering"]
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


/*
@Service
class FotruteService {

    fun getFotruter(): MutableList<Fotrute> {
        var df = DataFrame.read("3-fotruter.csv")
        val listofFotruter = df.rows().map { row ->
            val navn = row["rutenavn"].toString()
            val geometry: Any = row.get("geometry")!!
            val lengde = row["meter"].toString().toDouble()
            val ruteFølgerString = row["ruteFølger"].toString().replace("[", "").replace("]", "").replace("'", "")
            val ruteFølger = ruteFølgerString.split(", ")
            val merking = row["merking"].toString().toBoolean()
            val skilting = row["skilting"].toString().toBoolean()
            val gradering = row["gradering"].toString()
            print(geometry)
            Fotrute(navn = navn, geometri = geometry, lengde = lengde, ruteFølger = ruteFølger, merking = merking, skilting = skilting, gradering = gradering )
        }.toMutableList()
    return listofFotruter

}

}
*/
