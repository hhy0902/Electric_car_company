package com.example.electric_car_company.searchLoad


import com.google.gson.annotations.SerializedName

data class Guide(
    @SerializedName("distance")
    val distance: Int?,
    @SerializedName("duration")
    val duration: Int?,
    @SerializedName("instructions")
    val instructions: String?,
    @SerializedName("pointIndex")
    val pointIndex: Int?,
    @SerializedName("type")
    val type: Int?
)