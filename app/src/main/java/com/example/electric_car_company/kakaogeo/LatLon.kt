package com.example.electric_car_company.kakaogeo

import com.example.electric_car_company.geodata.Meta

import com.google.gson.annotations.SerializedName

data class LatLon(
    @SerializedName("documents")
    val documents: List<Document>?,
    @SerializedName("meta")
    val meta: Meta?
)