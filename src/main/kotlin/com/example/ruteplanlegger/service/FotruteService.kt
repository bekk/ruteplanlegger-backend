package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.Fotrute
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.rows
import org.jetbrains.kotlinx.dataframe.io.read
import org.springframework.stereotype.Service

@Service
class FotruteService {

    fun getFotruter(): MutableList<Fotrute> {
        var df = DataFrame.read("3-fotruter.csv")
        val listofFotruter = df.rows().map { row ->
            val navn = row["rutenavn"].toString()
            val geometry: Any = row["geometry"]!!.toString()
            val lengde = row["meter"].toString().toDouble()
            val ruteFølger = row["ruteFølger"].toString()
            val merking = row["merking"].toString()
            val skilting = row["skilting"].toString().toBoolean()

            Fotrute(navn = navn, geometri = geometry, lengde = lengde, ruteFølger = ruteFølger, merking = merking, skilting = skilting )
        }.toMutableList()
    return listofFotruter

}

}