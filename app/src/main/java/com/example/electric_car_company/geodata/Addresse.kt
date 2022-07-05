package com.example.electric_car_company.geodata

import com.google.gson.annotations.SerializedName

data class Addresse(
    @SerializedName("addressElements")
    val addressElements: List<AddressElement>?,
    @SerializedName("distance")
    val distance: Double?,
    @SerializedName("englishAddress")
    val englishAddress: String?,
    @SerializedName("jibunAddress")
    val jibunAddress: String?,
    @SerializedName("roadAddress")
    val roadAddress: String?,
    @SerializedName("x")
    val x: String?,
    @SerializedName("y")
    val y: String?
)