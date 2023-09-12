package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.Fotrute
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.rows
import org.jetbrains.kotlinx.dataframe.io.read
import org.springframework.stereotype.Service

@Service
class FotruteService {

    fun getFotrute(): MutableList<Fotrute> {
        var df = DataFrame.read("fotrute.csv")
        val listofFotruter = mutableListOf<Fotrute>()
        for (rute in df.rows()) {

            val navn = rute["rutenavn"].toString()
                .replace(Regex("[''\\[\\]]"), "") //['Blåmerket sti Østmerket'] -> "Blåmerket sti Østmarka"
            val rutenummer = rute["rutenummer"].toString()
                .replace(Regex("[''\\[\\]]"), "") //['['F_20170614_01']'] -> "F_20170614_01"
            val fotrute = Fotrute(navn = navn, rutenummer = rutenummer)

            listofFotruter.add(fotrute)


        }

        return listofFotruter

    }

}