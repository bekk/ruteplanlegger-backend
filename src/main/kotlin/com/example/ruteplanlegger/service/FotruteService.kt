package com.example.ruteplanlegger.service

import com.example.ruteplanlegger.model.Fotrute
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.rows
import org.jetbrains.kotlinx.dataframe.io.read
import org.springframework.stereotype.Service

@Service
class FotruteService {

    fun getFotruter(): MutableList<Fotrute> {
        var df = DataFrame.read("fotrute.csv")
        val listofFotruter = df.rows().map { row ->
            val navn = row["rutenavn"].toString().replace(Regex("[''\\[\\]]"), "")
            val rutenummer = row["rutenummer"].toString().replace(Regex("[''\\[\\]]"), "")
            Fotrute(navn = navn, rutenummer = rutenummer)
        }.toMutableList()
        print(df["id"])

    return listofFotruter

}

}