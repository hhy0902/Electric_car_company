package com.example.electric_car_company.searchLoad


import com.google.gson.annotations.SerializedName

data class LoadMap(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("currentDateTime")
    val currentDateTime: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("route")
    val route: Route?
)