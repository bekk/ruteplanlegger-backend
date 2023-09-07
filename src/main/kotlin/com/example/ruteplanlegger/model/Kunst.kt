package com.example.ruteplanlegger.model

data class Kunst(
    val id: Int = 0,
    val type: String = "Kunst",
    val tittel: String = "",
    val kunstType: String = "",
    val vegkategori: String = "",
    val vegnummer: Int = 0,
    val geometri: Geometri
)
