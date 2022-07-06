package com.example.electric_car_company.searchLoad


import com.google.gson.annotations.SerializedName

data class Route(
    @SerializedName("traoptimal")
    val traoptimal: List<Traoptimal>?
)