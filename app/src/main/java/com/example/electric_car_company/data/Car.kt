package com.example.electric_car_company.data

import com.google.gson.annotations.SerializedName

data class Car(
    @SerializedName("data")
    val `data`: MutableList<Data>?
)