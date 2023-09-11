package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.Fotrute
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.read
import org.springframework.stereotype.Service

@Service
class FotruteService {

    fun getFotrute(): MutableList<Fotrute> {


        var df = DataFrame.read("fotrute.csv")
        df.print()

        val listofFotruter = mutableListOf<Fotrute>()
        for(rute in df.rows()) {

            val navn = rute["rutenavn"].toString().replace(Regex("[''\\[\\]]"),"")
            val rutenummer = rute["rutenummer"].toString().replace(Regex("[''\\[\\]]"),"")
            val fotrute = Fotrute(navn = navn, rutenummer = rutenummer )

            listofFotruter.add(fotrute)


        }

        return listofFotruter

    }

}