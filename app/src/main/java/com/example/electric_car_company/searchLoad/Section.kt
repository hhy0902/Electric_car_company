package com.example.electric_car_company.searchLoad


import com.google.gson.annotations.SerializedName

data class Section(
    @SerializedName("congestion")
    val congestion: Int?,
    @SerializedName("distance")
    val distance: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("pointCount")
    val pointCount: Int?,
    @SerializedName("pointIndex")
    val pointIndex: Int?,
    @SerializedName("speed")
    val speed: Int?
)