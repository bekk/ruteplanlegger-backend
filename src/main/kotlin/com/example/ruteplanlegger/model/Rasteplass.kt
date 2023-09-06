package com.example.ruteplanlegger.model

data class Rasteplass(
    val id: Int = 0,
    val navn: String = "",
    val vegkategori: String = "",
    val vegnummer: Int = 0,
    val geometri: Geometri
)