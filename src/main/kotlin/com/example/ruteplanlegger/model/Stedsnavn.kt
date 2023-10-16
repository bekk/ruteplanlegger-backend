package com.example.ruteplanlegger.model

data class Stedsnavn (
    val navn: String,
    val id: Int = 0,
    val typeObjekt: String,
    val koordinat: LatLong,
)