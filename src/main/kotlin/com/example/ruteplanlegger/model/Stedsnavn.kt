package com.example.ruteplanlegger.model

import java.util.UUID

data class Stedsnavn (
    val navn: String,
    val id: UUID = UUID.randomUUID(),
    val fylke: String? = "",
    val kommune: String? = "",
    val typeObjekt: String,
    val koordinat: LatLong,
)