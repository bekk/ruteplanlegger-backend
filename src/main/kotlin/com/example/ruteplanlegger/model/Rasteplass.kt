package com.example.ruteplanlegger.model

data class Rasteplass(
    val id: Int = 0,
    val type: String = "Rasteplass",
    val navn: String = "",
    val vegkategori: String = "",
    val vegnummer: Int = 0,
    val geometri: Geometri,
    val toalett: Boolean?,
    val utemobler: Boolean?,
    val lekeapparat: Boolean?,
    val anbefalt: Boolean = false,
)