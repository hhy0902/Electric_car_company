package com.example.electric_car_company.searchLoad


import com.google.gson.annotations.SerializedName

data class Goal(
    @SerializedName("dir")
    val dir: Int?,
    @SerializedName("location")
    val location: List<Double>?
)