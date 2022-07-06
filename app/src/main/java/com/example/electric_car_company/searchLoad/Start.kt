package com.example.electric_car_company.searchLoad


import com.google.gson.annotations.SerializedName

data class Start(
    @SerializedName("location")
    val location: List<Double>?
)