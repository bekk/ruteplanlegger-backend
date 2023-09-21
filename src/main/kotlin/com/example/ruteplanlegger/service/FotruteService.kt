package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.Fotrute
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.rows
import org.jetbrains.kotlinx.dataframe.io.read
import org.jetbrains.kotlinx.dataframe.io.readCSV
import org.springframework.stereotype.Service

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