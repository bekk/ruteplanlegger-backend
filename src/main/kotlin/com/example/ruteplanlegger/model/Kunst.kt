package com.example.ruteplanlegger.model

data class Kunst(
    val id: Int = 0,
    val tittel: String = "",
    val type: String = "",
    val vegkategori: String = "",
    val vegnummer: Int = 0,
    val geometri: Geometri
)
