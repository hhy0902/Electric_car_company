package com.example.electric_car_company.data

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("carType")
    val carType: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("metro")
    val metro: String?,
    @SerializedName("rapidCnt")
    val rapidCnt: Int?,
    @SerializedName("slowCnt")
    val slowCnt: Int?,
    @SerializedName("stnAddr")
    val stnAddr: String?,
    @SerializedName("stnPlace")
    val stnPlace: String?
)